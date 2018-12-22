package com.compositeai.Emotionsong;
import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.SparseArray;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.content.Intent;
import android.provider.MediaStore;
import android.graphics.Bitmap;

import java.io.IOException;
import java.util.List;

import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import com.compositeai.predictivemodels.*;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
public class MainActivity extends AppCompatActivity {


    private Button button_gallery;
    private static final int CAMERA_REQUEST = 1888;
    private SquareImageView faceImageView;
    private TextView emotionShowView;
    private static final int MY_CAMERA_PERMISSION_CODE = 100;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_TAKE_PHOTO = 1;
    static final int PIXEL_WIDTH = 48;
    private final int GALLERY_CODE =1112;
    TensorFlowClassifier classifier;
    Button detect;
    private Bitmap temp;
    private float x1;
    private float y1;
    private int length;
    private String emotion="0";
    private final int PERMISSIONS_REQUEST_RESULT = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // 앱 기동 시 권한 체크 및 설정
        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
            }
        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("권한 설정을 하지 않으면 서비스를 이용 하실 수 없습니다.\n\n[설정] > [권한]에 가서 권한을 설정해주세요.")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .check();


        this.faceImageView = (SquareImageView) this.findViewById(R.id.facialImageView);
        Button photoButton = (Button) this.findViewById(R.id.phototaker);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });


        Button button_gallery= (Button) this.findViewById(R.id.gallery);
        button_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhotoFromGallary();
            }
        });

        detect = (Button) findViewById(R.id.detect);
        detect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                detectEmotion();
                detect.setEnabled(false);
            }
        });
        Button reset = (Button) findViewById(R.id.reset);
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearStatus();
            }
        });
        Button songsearch = (Button) findViewById(R.id.song);
        songsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(emotion.equals("0"))
                {
                    Toast.makeText(getBaseContext(), "감정찾기를 하시오", Toast.LENGTH_SHORT).show();
                }
                else if(emotion.equals("Happy"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/results?search_query=%EC%8B%A0%EB%82%98%EB%8A%94%EB%85%B8%EB%9E%98"));
                    startActivity(intent);
                }
                else if(emotion.equals("Sad"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/results?search_query=%EC%8A%AC%ED%94%8C%EB%95%8C+%EB%93%A3%EB%8A%94+%EB%85%B8%EB%9E%98"));
                    startActivity(intent);
                }
                else if(emotion.equals("Angry"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/results?search_query=%ED%99%94%EB%82%AC%EC%9D%84%EB%95%8C+%EB%93%A3%EB%8A%94+%EB%85%B8%EB%9E%98"));
                    startActivity(intent);
                }
                else if(emotion.equals("Fear")||emotion.equals("Surprise"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/results?search_query=%EB%A7%88%EC%9D%8C%EC%9D%84+%EC%A7%84%EC%A0%95%EC%8B%9C%ED%82%A4%EB%8A%94+%EC%9D%8C%EC%95%85"));
                    startActivity(intent);
                }
                else if(emotion.equals("Neutral"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/results?search_query=%EA%B0%90%EC%84%B1%EC%97%90+%EC%A0%96%EB%8A%94+%EB%85%B8%EB%9E%98"));
                    startActivity(intent);
                }
                else if(emotion.equals("Disgust"))
                {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("https://www.youtube.com/results?search_query=%EB%AD%98+%EA%B7%B8%EB%A0%87%EA%B2%8C%EA%B9%8C%EC%A7%80"));
                    startActivity(intent);
                }
            }
        });



        detect.setEnabled(false);
        this.emotionShowView = (TextView) findViewById(R.id.emotionTxtView);
        loadModel();

    }


    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_CODE);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private void loadModel() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier=TensorFlowClassifier.create(getAssets(), "CNN",
                            "opt_em_convnet_6000.pb", "labels.txt", PIXEL_WIDTH,
                            "input", "output_50", true, 7);

                } catch (final Exception e) {
                    //if they aren't found, throw an error!
                    throw new RuntimeException("Error initializing classifiers!", e);
                }
            }
        }).start();
    }

    /**
     * The main function for emotion detection
     */
    private void detectEmotion(){





        FaceDetector faceDetector = new
                FaceDetector.Builder(getApplicationContext()).setTrackingEnabled(false)
                .build();
        if(!faceDetector.isOperational()){
            Toast.makeText(getBaseContext(), "Face Detector Not build!", Toast.LENGTH_SHORT).show();
            return;
        }
        Bitmap image=((BitmapDrawable)faceImageView.getDrawable()).getBitmap();
        Frame frame = new Frame.Builder().setBitmap(image).build();
        SparseArray<Face> faces = faceDetector.detect(frame);
        if(faces.size()==0)
        {
            Toast.makeText(getBaseContext(), "얼굴이 감지 안됨", Toast.LENGTH_SHORT).show();
        }
        else {
            for (int i = 0; i < faces.size(); i++) {
                Face thisFace = faces.valueAt(i);
                x1 = thisFace.getPosition().x + (float) 0.10*thisFace.getWidth();
                y1 = thisFace.getPosition().y + thisFace.getHeight() - (float)0.80*thisFace.getWidth();
                float x2 = x1 + (float)0.80*thisFace.getWidth();
                float y2 = y1 + (float)0.80*thisFace.getWidth();
                length = (int)(0.80*thisFace.getWidth());
                // 얼굴 크로핑 후 128픽셀로 변환
                Bitmap cropFace = Bitmap.createBitmap(image, (int)x1, (int)y1,length,length);
                temp =cropFace;
            }
            Bitmap grayImage = toGrayscale(temp);
            Bitmap resizedImage=getResizedBitmap(grayImage,48,48);
            int pixelarray[];

            //Initialize the intArray with the same size as the number of pixels on the image
            pixelarray = new int[resizedImage.getWidth()*resizedImage.getHeight()];

            //copy pixel data from the Bitmap into the 'intArray' array
            resizedImage.getPixels(pixelarray, 0, resizedImage.getWidth(), 0, 0, resizedImage.getWidth(), resizedImage.getHeight());


            float normalized_pixels [] = new float[pixelarray.length];
            for (int i=0; i < pixelarray.length; i++) {
                // 0 for white and 255 for black
                int pix = pixelarray[i];
                int b = pix & 0xff;
                //  normalized_pixels[i] = (float)((0xff - b)/255.0);
                // normalized_pixels[i] = (float)(b/255.0);
                normalized_pixels[i] = (float)(b);

            }
            System.out.println(normalized_pixels);
            Log.d("pixel_values",String.valueOf(normalized_pixels));
            String text=null;

            try{
                final Classification res = classifier.recognize(normalized_pixels);
                //if it can't classify, output a question mark
                if (res.getLabel() == null) {
                    text = "당신의감정은: "+ ": \n";
                } else {
                    //else output its name
                    text = String.format("%s: %s, %f\n", "당신의감정은: ", res.getLabel(),
                            res.getConf());
                    emotion = res.getLabel();

                }}
            catch (Exception  e){
                System.out.print("Exception:"+e.toString());

            }
            Toast.makeText(getBaseContext(), emotion, Toast.LENGTH_SHORT).show();


            this.faceImageView.setImageBitmap(grayImage);
            this.emotionShowView.setText(text);
        }

    }


    /**
     *
     */
    private void clearStatus(){
        detect.setEnabled(false);
        this.faceImageView.setImageResource(R.drawable.ic_launcher_background);
        this.emotionShowView.setText("Status: ?");
        emotion="0";
    }

    /**
     *
     * @param bmpOriginal
     * @return
     */

    public Bitmap toGrayscale(Bitmap bmpOriginal)
    {
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();

        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }

    //https://stackoverflow.com/questions/15759195/reduce-size-of-bitmap-to-some-specified-pixel-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            detect.setEnabled(true);
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            faceImageView.setImageBitmap(imageBitmap);
        }
        else if(resultCode ==RESULT_OK && requestCode==GALLERY_CODE){
            detect.setEnabled(true);
            sendPicture(data.getData());
        }


    }

    private void sendPicture(Uri imgUri) {

        String imagePath = getRealPathFromURI(imgUri); // path 경로
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        int exifOrientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        int exifDegree = exifOrientationToDegrees(exifOrientation);

        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);//경로를 통해 비트맵으로 전환
        faceImageView.setImageBitmap(rotate(bitmap, exifDegree));//이미지 뷰에 비트맵 넣기

    }

    private String getRealPathFromURI(Uri contentUri) {
        int column_index=0;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        }

        return cursor.getString(column_index);
    }
    private int exifOrientationToDegrees(int exifOrientation) {
        if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_90) {
            return 90;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_180) {
            return 180;
        } else if (exifOrientation == ExifInterface.ORIENTATION_ROTATE_270) {
            return 270;
        }
        return 0;
    }
    private Bitmap rotate(Bitmap src, float degree) {

// Matrix 객체 생성
        Matrix matrix = new Matrix();
// 회전 각도 셋팅
        matrix.postRotate(degree);
// 이미지와 Matrix 를 셋팅해서 Bitmap 객체 생성
        return Bitmap.createBitmap(src, 0, 0, src.getWidth(),
                src.getHeight(), matrix, true);
    }



}

