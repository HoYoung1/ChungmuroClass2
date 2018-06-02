package com.chungmuroclass.chungmuroclass.fragment;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobile.client.AWSMobileClient;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.chungmuroclass.chungmuroclass.ActivitySignUpName;
import com.chungmuroclass.chungmuroclass.R;
import com.chungmuroclass.chungmuroclass.Util.PermissionUtility;
import com.chungmuroclass.chungmuroclass.globals.Globals;
import com.chungmuroclass.chungmuroclass.globals.constant.CommonConst;
import com.chungmuroclass.chungmuroclass.globals.constant.URLs;
import com.chungmuroclass.chungmuroclass.model.IsExist;
import com.google.gson.Gson;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentProfile extends Fragment {


    private View v;
    private ImageView imgProfile;
    private TextView txtName;
    private TextView txtEditImg;
    private RelativeLayout main_waiting_cover;
    private IsExist isExist;
    private Gson gson;

    //사진에 필요한요소 묶기
    private Uri photoURI;
    private String mCurrentPhotoPath;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Uri albumURI;
    private static final int REQUEST_IMAGE_CROP = 2222;
    private Bitmap bm;
    private String NameOfFile = "";
    private String NameOfFIleWithEX = "";
    //s3에필요한 요소 묶기
    private AmazonS3Client s3Client;
    private BasicAWSCredentials credentials;

    //카메라때문에 필요한거
    private static final int MY_PERMISSION_CAMERA = 3333;



    public  static FragmentProfile newInstance() {
        return new FragmentProfile();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        v = inflater.inflate(R.layout.fragment_fragment_profile, container, false);


        AWSMobileClient.getInstance().initialize(getActivity()).execute();

        credentials = new BasicAWSCredentials(CommonConst.S3KEY, CommonConst.S3SECRET);
        s3Client = new AmazonS3Client(credentials);

        setLayout();
        setInit();
        return v;
    }

    private void setInit() {
        Glide.with(getActivity())
                .load(URLs.STORAGE.getValue()+ Globals.img_url)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        main_waiting_cover.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        main_waiting_cover.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(imgProfile);
        txtName.setText(Globals.userName);

        txtEditImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermission();
                getDialogSelectCamera();
            }
        });
    }

    private void setLayout() {
        imgProfile=(ImageView)v.findViewById(R.id.imgProfile);
        txtName=(TextView)v.findViewById(R.id.txtName);
        main_waiting_cover = (RelativeLayout)v.findViewById(R.id.main_waiting_cover);
        txtEditImg = (TextView)v.findViewById(R.id.txtEditImg);
        gson = new Gson();
    }


    void doPutEditInfo(String url) throws IOException {
        main_waiting_cover.setVisibility(View.VISIBLE);

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        Map<String, String> params = new HashMap<String, String>();


        params.put("student_id", Globals.student_id);
        params.put("img_url", NameOfFIleWithEX);

        Log.d("호영아student_id", Globals.student_id);
        Log.d("호영아img_url", NameOfFIleWithEX);

        JSONObject parameter = new JSONObject(params);

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(JSON, parameter.toString());
        Request request = new Request.Builder()
                .url(url)
                .put(body)
                .addHeader("content-type", "application/json; charset=utf-8")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("response", call.request().body().toString());
                Log.d("hyfortest", "에러 ㅠ");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        main_waiting_cover.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "서버와 통신실패", Toast.LENGTH_SHORT).show();

                    }
                });


            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                Log.d("hyfortest", "성공^^;");
                Log.d("호영아", result);
                //TO DO
                // akey값 가져와서 sharedpreference 저장
                // 원래는 SignUpNoti로 넘겨야 하나,뒷과정생략이라생각후 ActivityLogin을 실행시킨다.
                //  ActivityLogin에서는 항상 akey존재유무를 따지며 ActivityMain으로 넘길지를 판단한다

                try {
                    //gson으로 객체에넣어버리기

                    isExist = gson.fromJson(result, IsExist.class);


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (isExist != null) {
                                if (isExist.getExist() == true) {
                                    Toast.makeText(getActivity(), "이미지 수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                    Globals.img_url = NameOfFIleWithEX;


                                } else {
                                    Toast.makeText(getActivity(), "이미지 수정이 실패했습니다..", Toast.LENGTH_SHORT).show();

                                }
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();

                } finally {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            main_waiting_cover.setVisibility(View.GONE);

                        }
                    });


                }


            }


        });


    }


    //이밑으로는 카메라 함수임 이해하려하지말자..

    private void imageToS3() {

        main_waiting_cover.setVisibility(View.VISIBLE);

        // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
        Uri tempUri = getImageUri(getActivity().getApplicationContext(), bm);
        Log.d("호영아tempUri", tempUri.getPath());


        // CALL THIS METHOD TO GET THE ACTUAL PATH
        File finalFile = new File(getRealPathFromURI(tempUri));

        Log.d("호영아 finalFile.getpath", finalFile.getPath() + "");


        TransferUtility transferUtility =
                TransferUtility.builder()
                        .context(getActivity().getApplicationContext())
                        .awsConfiguration(AWSMobileClient.getInstance().getConfiguration())
                        .s3Client(s3Client)
                        .build();


        ObjectMetadata md = new ObjectMetadata();
        md.setContentType("image/png");
//https://stackoverflow.com/questions/23467044/how-to-set-the-content-type-of-an-s3-object-via-the-sdk


        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

        Random generator = new Random();

        // int 값의 범위에서 정수 랜덤값을 추출한다.
        int randomint = generator.nextInt();

        NameOfFile = timeStamp + "_" + randomint;
        Log.d("호영아s3에파일이름NameOfFile", NameOfFile);

        TransferObserver uploadObserver =
                transferUtility.upload(NameOfFile + "." + getFileExtension(tempUri), finalFile, md, CannedAccessControlList.PublicRead);

        uploadObserver.setTransferListener(new TransferListener() {
            @Override
            public void onStateChanged(int id, TransferState state) {
                if (TransferState.COMPLETED == state) {
                    // Handle a completed download.
                    main_waiting_cover.setVisibility(View.GONE);
                    //프로그레스바를 지움
                    Log.d("호영아", "s3에 올리기성공햇음");
                    try {
                        doPutEditInfo(URLs.FACEIMG_CHANGE.getValue());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {

            }

            @Override
            public void onError(int id, Exception ex) {
                main_waiting_cover.setVisibility(View.GONE);
                //프로그레스바를 지움
                Log.d("호영아", "s3에 안올라갔다................줫땠다 이거찍혀있으면노답심각");

            }
        });
        Log.d("호영아s3test", "jsaS3/" + NameOfFile + "." + getFileExtension(tempUri));
        NameOfFIleWithEX = NameOfFile + "." + getFileExtension(tempUri);
        Log.d("호영아s3filename", NameOfFIleWithEX);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }


    private void getDialogSelectCamera() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater().inflate(R.layout.dialogcamera, null);

        //다이알로그에있는 버튼
        ConstraintLayout btnTakePhoto;
        ConstraintLayout btnChoosePhoto;

        //다이알로그
        final AlertDialog dialog;


        btnTakePhoto = (ConstraintLayout) mView.findViewById(R.id.layout1);
        btnChoosePhoto = (ConstraintLayout) mView.findViewById(R.id.layout2);

        mBuilder.setView(mView);
        dialog = mBuilder.create();

        //https://stackoverflow.com/questions/28937106/how-to-make-custom-dialog-with-rounded-corners-in-android/28937224
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //for Shape
        dialog.show();

        //size설정을위해
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        dialog.getWindow().setLayout((3 * width) / 4, (1 * height) / 4);


        btnTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                cameraIntent();
            }
        });

        btnChoosePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                galleryIntent();

            }
        });
    }


    //카메라 인텐트
    private void cameraIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(getActivity(),
                        "com.chungmuroclass.chungmuroclass.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CAMERA);
            }
        }
    }

    //갤러리 인텐트
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    //카메라 갤러리 둘다 => 크롭으로 넘어감
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == Activity.RESULT_OK) {

            //갤러리에서온것
            if (requestCode == SELECT_FILE)

            {

                onSelectFromGalleryResult(data);
                //dialog.dismiss();//다이알로그끄기위해서 넣음


                //카메라에서온것
            } else if (requestCode == REQUEST_CAMERA) {


                //        Bitmap bm = BitmapFactory.decodeFile(mCurrentPhotoPath);
                //        ivImage.setImageBitmap(bm); 하울코드 , 이렇게안했는데 나중에 에러나면바꾸자

                try {
                    File albumFile = null;
                    albumFile = createImageFile();
                    albumURI = Uri.fromFile(albumFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                onCaptureImageResult(data);
                //dialog.dismiss();


                //갤러리카메라둘다 크롭으로넘어감
            } else if (requestCode == REQUEST_IMAGE_CROP) {
                if (resultCode == Activity.RESULT_OK) {


                    setImageView();
                    imageToS3();

                }
            }
        }
    }


    //프로필이미지뷰를 셋팅
    private void setImageView() {
        //ivImage.setImageURI(albumURI);
        Log.d("호영아 크롭된후 albumURI", albumURI.getPath());

        bm = BitmapFactory.decodeFile(albumURI.getPath());
        imgProfile.setImageBitmap(bm);

    }


    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {


        if (data.getData() != null) {
            try {
                File albumFile = null;
                albumFile = createImageFile();
                photoURI = data.getData();
                albumURI = Uri.fromFile(albumFile);
                cropImage();
            } catch (Exception e) {
                Log.e("TAKE_ALBUM_SINGLE ERROR", e.toString());
            }
        }

    }

    private void onCaptureImageResult(Intent data) {

        //다주석임 !!crop 으로 카메라 찍히면 intent만넘김
        cropImage();
    }

    public void cropImage() {
        Log.i("cropImage", "Call");
//        Log.i("cropImage", "photoURI : " + photoURI + " / albumURI : " + albumURI);

        Intent cropIntent = new Intent("com.android.camera.action.CROP");

        // 50x50픽셀미만은 편집할 수 없다는 문구 처리 + 갤러리, 포토 둘다 호환하는 방법
        cropIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        cropIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        cropIntent.setDataAndType(photoURI, "image/*");
        //cropIntent.putExtra("outputX", 200); // crop한 이미지의 x축 크기, 결과물의 크기
        //cropIntent.putExtra("outputY", 200); // crop한 이미지의 y축 크기
        cropIntent.putExtra("aspectX", 1); // crop 박스의 x축 비율, 1&1이면 정사각형
        cropIntent.putExtra("aspectY", 1); // crop 박스의 y축 비율
        cropIntent.putExtra("scale", true);
        cropIntent.putExtra("output", albumURI); // 크랍된 이미지를 해당 경로에 저장
        startActivityForResult(cropIntent, REQUEST_IMAGE_CROP);
    }


    //공식문서 복사
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("호영아mCurrentPhotoPath", mCurrentPhotoPath);
        return image;
    }



    //카메라와 갤러리를 위한 권한 획득.어디넣을지 , 카메라를 눌렀을때 들어갈지 고민해봐야할듯.
    private void checkPermission(){
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // 처음 호출시엔 if()안의 부분은 false로 리턴 됨 -> else{..}의 요청으로 넘어감
            if ((ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) ||
                    (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), android.Manifest.permission.CAMERA))) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("알림")
                        .setMessage("저장소 권한이 거부되었습니다. 사용을 원하시면 설정에서 해당 권한을 직접 허용하셔야 합니다.")
                        .setNeutralButton("설정", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse("package:" + getActivity().getPackageName()));
                                startActivity(intent);
                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getActivity().finish();
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA}, MY_PERMISSION_CAMERA);
            }
        }
    }

    //권한 설정결과
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case  PermissionUtility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            /*        if(userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();*/
                } else {
                    //code for deny
                    Toast.makeText(getActivity(), "must need permission", Toast.LENGTH_SHORT).show();
                    return;
                }
                break;
        }
    }


}
