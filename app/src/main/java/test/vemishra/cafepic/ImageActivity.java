package test.vemishra.cafepic;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Environment;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;

import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.Future;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageActivity extends AppCompatActivity {
    public static final int Camera_Request = 10;
    private ImageView img1;
    private Button button1;
    private String mCurrentPhotoPath;
    private Uri file;
    private Button button2;
    private String ImageDecode;
    private Bitmap bitmap1;
    private Bitmap bitmap;
    private String Item_id,Item_name,Item_description,Item_cal,Item_price1,Item_quan,Item_price_icon,Item_category,Item_imageUrl,Item_formattedImage,url;
    private  Boolean isVeg,servedOnMonday,servedOnTuesday,servedOnWednesday,servedOnThursday,servedOnFriday;
    private Button upload;
    private Double Item_price;
    File pictureFile,originalfile;
    Bundle saveIntent;
    private Button tryAgain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);
        if (savedInstanceState == null) {
            saveIntent = getIntent().getExtras();
        }
        else{
            saveIntent = savedInstanceState.getBundle("saveIntent");
        }
        instan();


    }

    public void BtnTakePicClicked(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        originalfile = getOutputMediaFile();
        file = Uri.fromFile(originalfile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, file);
        startActivityForResult(intent, 100);
    }
    public void BtnPickerClicked(View v){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 0);
    }
    public void tryAgainClicked(View v){
        tryAgain.setVisibility(View.INVISIBLE);
        upload.setVisibility(View.INVISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
    }
    public void UploadClicked(View v) {
        if (pictureFile!=null){
            Future uploading = Ion.with(ImageActivity.this)
                    .load("http://10.77.133.54:8080/uploads/")
                    .setMultipartFile("Key", pictureFile)
                    .asString()
                    .withResponse()
                    .setCallback(new FutureCallback<Response<String>>() {
                        @Override
                        public void onCompleted(Exception e, Response<String> result) {
                            try {
                                JSONObject jobj = new JSONObject(result.getResult());
                                Item_formattedImage = jobj.getString("response");
                                Toast.makeText(getApplicationContext(), jobj.getString("response"), Toast.LENGTH_SHORT).show();
                                Future loading = Ion.with(ImageActivity.this)
                                        .load("http://10.77.133.54:8080/uploads/")
                                        .setMultipartFile("Key", originalfile)
                                        .asString()
                                        .withResponse()
                                        .setCallback(new FutureCallback<Response<String>>() {
                                            @Override
                                            public void onCompleted(Exception e, Response<String> result) {
                                                try {
                                                    JSONObject jobj = new JSONObject(result.getResult());
                                                    Item_imageUrl = jobj.getString("response");
                                                    uploadToMongo();

                                                    Toast.makeText(getApplicationContext(), jobj.getString("response"), Toast.LENGTH_SHORT).show();

                                                } catch (JSONException e1) {
                                                    e1.printStackTrace();
                                                }

                                            }
                                        });

                            } catch (JSONException e1) {
                                e1.printStackTrace();
                            }

                        }
                    });
    }else{
            Toast.makeText(this,"Select a picture!",Toast.LENGTH_LONG).show();
        }

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 100) {
                if (resultCode == RESULT_OK) {
                    tryAgain.setVisibility(View.VISIBLE);
                    upload.setVisibility(View.VISIBLE);
                    button1.setVisibility(View.INVISIBLE);
                    button2.setVisibility(View.INVISIBLE);
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),file);
                    addPhotoToGallery();
                    Bitmap bitmap_Empty;
                    bitmap_Empty = createBitmap(bitmap);
                    saveBitmap(bitmap_Empty);
                }
            }

            if (requestCode == 0 && resultCode == RESULT_OK
                    && null != data) {
                tryAgain.setVisibility(View.VISIBLE);
                upload.setVisibility(View.VISIBLE);
                button1.setVisibility(View.INVISIBLE);
                button2.setVisibility(View.INVISIBLE);
                bitmap  = BitmapFromData(data);
               String path = ImageFilePath.getPath(this,data.getData());
                originalfile = new File(path);
                Bitmap bitmap_Empty;
                bitmap_Empty = createBitmap(bitmap);
                saveBitmap(bitmap_Empty);

            }
        } catch (Exception e) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_LONG)
                    .show();
        }

    }
    public boolean saveBitmap(Bitmap b){
        pictureFile = getOutputMediaFile();
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            b.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
            Toast.makeText(getApplicationContext(),"sucess",Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            //  Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            // Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        file = Uri.fromFile(pictureFile);
        addPhotoToGallery();
        return true;
    }
    public Bitmap BitmapFromData(Intent data){
        Uri URI = data.getData();
        String[] FILE = { MediaStore.Images.Media.DATA };


        Cursor cursor = getContentResolver().query(URI,
                FILE, null, null, null);

        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(FILE[0]);
        ImageDecode = cursor.getString(columnIndex);
        cursor.close();
        bitmap = BitmapFactory.decodeFile(ImageDecode);
        return bitmap;
    }
    public Bitmap createBitmap(Bitmap createdBitmap){
        bitmap1 =  getResizedBitmap(createdBitmap, 306,306);
        Bitmap bitmap_Empty = Bitmap.createBitmap(306/*width*/, 534/*height*/, Bitmap.Config.ARGB_8888);
        View v = new MyCanvas(getApplicationContext(),Item_name,Item_description,Item_quan,Item_cal,Item_price1,Item_price_icon);
        Canvas canvas = new Canvas(bitmap_Empty);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap1,0,0,null);
        v.draw(canvas);
        img1.setImageBitmap(bitmap_Empty);
        return bitmap_Empty;
    }
    public Bitmap getResizedBitmap(Bitmap image, int bitmapWidth, int bitmapHeight) {
        return Bitmap.createScaledBitmap(image, bitmapWidth, bitmapHeight, true);
    }
    private void addPhotoToGallery() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(file); //your file uri
        this.sendBroadcast(mediaScanIntent);
    }

    private static File getOutputMediaFile(){
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()){
            if (!mediaStorageDir.mkdirs()){
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator +
                "IMG_"+ timeStamp + ".gif");
    }

    void uploadToMongo() {
        // Tag used to cancel the request
        JsonObject json = new JsonObject();
        json.addProperty("name", Item_name);
        json.addProperty("price", Item_price);
        json.addProperty("description", Item_description );
        json.addProperty("category",Item_category);
        json.addProperty("isVeg",isVeg);
        Item_imageUrl = Item_imageUrl.replaceAll("\\s+","");
        json.addProperty("imageUrl",Item_imageUrl);
        json.addProperty("formattedImage",Item_formattedImage);
        json.addProperty("servedOnMonday",servedOnMonday);
        json.addProperty("servedOnTuesday",servedOnTuesday);
        json.addProperty("servedOnWednesday",servedOnWednesday);
        json.addProperty("servedOnThursday",servedOnThursday);
        json.addProperty("servedOnFriday",servedOnFriday);
        Ion.with(ImageActivity.this)
                .load("PUT","http://10.77.133.54:8090/api/products/"+Item_id)
                .setJsonObjectBody(json)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        // do stuff with the result or error
                        Intent intent = new Intent(ImageActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    //    Toast.makeText(getApplicationContext(),"cheers to good life",Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void instan(){
        Item_name = saveIntent.getString("Name");
        Item_description = saveIntent.getString("Description");
        Item_cal = 225+"";
        Item_price = saveIntent.getDouble("Price");
        Item_price1=getString(R.string.Rs)+Item_price;
        Item_quan = 400+"g";
        Item_category = saveIntent.getString("category");
        isVeg = saveIntent.getBoolean("isVeg");
        Item_imageUrl = saveIntent.getString("imageUrl");
        Item_formattedImage = saveIntent.getString("formattedImage");
        servedOnMonday = saveIntent.getBoolean("servedOnMonday");
        servedOnTuesday = saveIntent.getBoolean("servedOnTuesday");
        servedOnWednesday = saveIntent.getBoolean("servedOnWednesday");
        servedOnThursday = saveIntent.getBoolean("servedOnThursday");
        servedOnFriday = saveIntent.getBoolean("servedOnFriday");
        Item_id = saveIntent.getString("_id");
        button1 = (Button)findViewById(R.id.button_image);
        button2 = (Button)findViewById(R.id.button_picker);
        img1  =(ImageView)findViewById(R.id.imageview);
        upload =(Button)findViewById(R.id.uploadimg);
        tryAgain =(Button)findViewById(R.id.tryAgain);
        tryAgain.setVisibility(View.INVISIBLE);
        upload.setVisibility(View.INVISIBLE);
        button1.setVisibility(View.VISIBLE);
        button2.setVisibility(View.VISIBLE);
    }
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(saveIntent==null){
            Toast.makeText(this,"lootgye",Toast.LENGTH_LONG).show();
        }
        outState.putBundle("saveIntent",saveIntent);
        super.onSaveInstanceState(outState);
    }
}
