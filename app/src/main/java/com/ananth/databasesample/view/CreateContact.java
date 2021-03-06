package com.ananth.databasesample.view;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ananth.databasesample.R;
import com.ananth.databasesample.model.local.ContactModel;
import com.ananth.databasesample.utils.Utils;
import com.ananth.databasesample.viewmodel.AddContactViewModel;
import com.ananth.databasesample.viewmodel.ViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.inject.Inject;

public class CreateContact extends AppCompatActivity {

    private static final int REQUEST_CAMERA_PERMISSION = 101;
    private static final int REQUEST_WRITE_PERMISSION = 102;
    private static final int REQUEST_READ_PERMISSION = 103;
    private Toolbar toolbar;
    private EditText mName;
    private EditText mEmail;
    private EditText mPhone;
    private EditText mLocation;
    private Button mCreate;
    private ImageView mImage;
    private String mSelectedFilePath = "";
    private File file;
    byte[] photo = null;
    String mPhoto = "";
    private String mType = "";
    private String mNameVal = "";
    private String mEmailVal = "";
    private String mLocationVal = "";
    private String mPhoneVal = "";
    private String mImageUri = "";
    private TextView mHeader;
    private AddContactViewModel viewModel;
    @Inject
    ViewModelFactory viewModelFactory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_contact);
        mHeader = (TextView) findViewById(R.id.header_title);
        mName = (EditText) findViewById(R.id.name);
        mEmail = (EditText) findViewById(R.id.email);
        mPhone = (EditText) findViewById(R.id.phone);
        mCreate = (Button) findViewById(R.id.create_btn);
        mLocation = (EditText) findViewById(R.id.location);
        mImage = (ImageView) findViewById(R.id.profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        viewModel = ViewModelProviders.of(this).get(AddContactViewModel.class);
        if (getIntent() != null) {
            mType = getIntent().getStringExtra("type");
            mNameVal = getIntent().getStringExtra("name");
            mEmailVal = getIntent().getStringExtra("email");
            mLocationVal = getIntent().getStringExtra("location");
            mPhoneVal = getIntent().getStringExtra("phone");
            if (!TextUtils.isEmpty(getIntent().getStringExtra("image"))) {
                mImageUri = getIntent().getStringExtra("image");
            }
        }

        if (mType.equals("edit")) {
            mName.setText(mNameVal);
            mEmail.setText(mEmailVal);
            mPhone.setText(mPhoneVal);
            mLocation.setText(mLocationVal);
            mImage.setImageURI(Uri.parse(mImageUri));
            mSelectedFilePath = mImageUri;
            mCreate.setText("Update");
            mHeader.setText("Edit Contact");
            mPhone.setEnabled(false);
        } else {
            mCreate.setText("Create");
            mHeader.setText("Create Contact");
        }
        mCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("type:" + mType);
                if (mType.equals("edit")) {
                    insertContactandUpdateContact(mType);
                } else {
                    insertContactandUpdateContact(mType);
                }
            }
        });


        mImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeImage();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return true;
    }

    private void insertContactandUpdateContact(String type) {
        if (!TextUtils.isEmpty(mName.getText().toString().trim())) {
            if (!TextUtils.isEmpty(mEmail.getText().toString().trim())) {
                if (!TextUtils.isEmpty(mPhone.getText().toString().trim())) {
                    if (!TextUtils.isEmpty(mLocation.getText().toString().trim())) {
                        System.out.println("type:" + type);
                        if (type.equals("edit")) {
                            viewModel.updateContact(new ContactModel(mName.getText().toString().trim(), mEmail.getText().toString().trim(), mPhone.getText().toString().trim(), mLocation.getText().toString().trim(), mSelectedFilePath.toString()));
                            finish();
                        } else {
                            System.out.println("addContact:" + "activity");
                            try {
                                if (!viewModel.isContactExist(mPhone.getText().toString().trim())) {
                                    viewModel.addContact(new ContactModel(mName.getText().toString().trim(), mEmail.getText().toString().trim(), mPhone.getText().toString().trim(), mLocation.getText().toString().trim(), mSelectedFilePath.toString()));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Contact already exist!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        Toast.makeText(CreateContact.this, "Enter your location", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(CreateContact.this, "Enter your phone", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(CreateContact.this, "Enter your email", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(CreateContact.this, "Enter your name", Toast.LENGTH_SHORT).show();
        }
    }


    public void takeImage() {
        AlertDialog.Builder ab = new AlertDialog.Builder(CreateContact.this);
        ab.setTitle("Picture");
        ab.setItems(getResources().getStringArray(R.array.items),
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int choice) {
                        // TODO Auto-generated method stub
                        if (choice == 0) {

                            if (ActivityCompat.checkSelfPermission(CreateContact.this, Manifest.permission.CAMERA)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // Check Permissions Now
                                ActivityCompat.requestPermissions(CreateContact.this,
                                        new String[]{Manifest.permission.CAMERA},
                                        REQUEST_CAMERA_PERMISSION);
                            } else {
                                Intent intent = new Intent(
                                        MediaStore.ACTION_IMAGE_CAPTURE);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    File photo = new File(Environment
                                            .getExternalStorageDirectory(),
                                            "Journal" + Utils.getID() + ".jpeg");
                                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                                            Uri.fromFile(photo));
                                    mSelectedFilePath = photo.getAbsolutePath();
                                    startActivityForResult(intent,
                                            Utils.ACTION_TAKE_PICTURE);
                                }
                            }

                        } else {

                            if (Utils.mBitmap != null) {
                                Utils.mBitmap = null;
                            }
                            if (ActivityCompat.checkSelfPermission(CreateContact.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                    != PackageManager.PERMISSION_GRANTED) {
                                // Check Permissions Now
                                ActivityCompat.requestPermissions(CreateContact.this,
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        REQUEST_READ_PERMISSION);
                            } else {
                                Intent intent = new Intent();
                                intent.setType("image/*");
                                intent.setAction(Intent.ACTION_GET_CONTENT);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    startActivityForResult(Intent.createChooser(
                                            intent, "Select Image"),
                                            Utils.ACTION_GALLERY);
                                }

                            }
                        }

                    }
                });
        ab.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        try {
            if (resultCode == RESULT_OK) {
                if (requestCode == Utils.ACTION_GALLERY) {

                    if (ActivityCompat.checkSelfPermission(CreateContact.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        ActivityCompat.requestPermissions(CreateContact.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_PERMISSION);
                    } else {
                        System.out.println("data.getData:" + data.getData());
                        Uri selectedImageUri = data.getData();
                        System.out.println("19 image uri :" + selectedImageUri);
                        if (Build.VERSION.SDK_INT >= 19) {
                            System.out.println("greater 19:" + "kitkat");
//						mSelectedFilePath = getRealPathFromURI_API19(
//								getApplicationContext(), selectedImageUri);
                            System.out.println("greater 19 image uri :" + selectedImageUri);
//                        mSelectedFilePath = getPathOfImage(selectedImageUri);

                            mSelectedFilePath = getImagePath(selectedImageUri);
                            System.out.println("mSelectedFissslssePath res" + mSelectedFilePath);
                        } else {
                            System.out.println("greater 19:" + "not kitkat");
                            mSelectedFilePath = getPathOfImage(selectedImageUri);
//                            mImage.setImageURI(selectedImageUri);
                            System.out.println("mSelectedFissslePath res" + mSelectedFilePath);
                        }
                        new imageRotateAsync().execute();
                    }

                } else if (requestCode == Utils.ACTION_TAKE_PICTURE) {
                    // flag = true;
                    // new Utils();
                    if (ActivityCompat.checkSelfPermission(CreateContact.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        // Check Permissions Now
                        ActivityCompat.requestPermissions(CreateContact.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                REQUEST_WRITE_PERMISSION);
                    } else {
                        // permission has been granted, continue as usual
                        new imageRotateAsync().execute();
                    }


                }

            }
        } catch (Exception e) {
        }
    }

    public String getImagePath(Uri uri) {
        String path = "";
        try {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            String document_id = cursor.getString(0);
            document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
            cursor.close();

            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
            cursor.moveToFirst();
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            System.out.println("path111:" + path);
            cursor.close();
        } catch (Exception e) {

        }


        return path;
    }

    @SuppressLint("NewApi")
    private String getPathOfImage(Uri uri) {
        String wholeID = DocumentsContract.getDocumentId(uri);

        System.out.println("WholeId:" + wholeID);

        // Split at colon, use second item in the array
        String id = wholeID.split(":")[1];

        String[] column = {MediaStore.Images.Media.DATA};

        // where id is equal to
        String sel = MediaStore.Images.Media._ID + "=?";

        Cursor cursor = getContentResolver().
                query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

        String filePath = "";

        int columnIndex = cursor.getColumnIndex(column[0]);

        if (cursor.moveToFirst()) {
            filePath = cursor.getString(columnIndex);
        }

        System.out.println("File Path1:" + filePath);
        cursor.close();
        return filePath;
    }

    private class imageRotateAsync extends AsyncTask<Void, Void, Integer> {

        private boolean mFlag;
        private String mProfilePicture;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(Void... params) {
            // TODO Auto-generated method stub
            if (imageRotation()) {
                return 0;
            }
            return 1;
        }

        @Override
        protected void onPostExecute(Integer result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (result == 0) {
                mImage.setImageURI(Uri.parse(mSelectedFilePath.toString()));

            }
        }
    }

    @SuppressLint("ShowToast")
    private boolean imageRotation() {
        // TODO Auto-generated method stub

        try {
            File f = new File(mSelectedFilePath);
            ExifInterface exif = new ExifInterface(f.getPath());
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            int angle = 0;
            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                angle = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                angle = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                angle = 270;
            }
            Matrix mat = new Matrix();
            mat.postRotate(angle);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Utils.getLolliPopBitmap(mSelectedFilePath);
            Bitmap bmp = BitmapFactory.decodeStream(new FileInputStream(f),
                    null, null);
            if (Utils.mBitmap != null) {
                Utils.mBitmap.recycle();
            }
            System.gc();

            Utils.mBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(),
                    bmp.getHeight(), mat, true);
            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                Utils.mBitmap.compress(Bitmap.CompressFormat.JPEG, 60, bytes);
                file = new File(Utils.FOLDER_PATH + "/" + Utils.getID()
                        + ".jpg");
                file.createNewFile();
                Utils.getLolliPopBitmap(mSelectedFilePath);
                FileOutputStream fo = new FileOutputStream(file);
                fo.write(bytes.toByteArray());
                fo.close();
                mSelectedFilePath = file.getAbsolutePath();
                System.out.println("path11:" + mSelectedFilePath);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
        } catch (OutOfMemoryError oom) {
        } catch (Exception e) {

        }
        return true;
    }

    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                Intent intent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    File photo = new File(Environment
                            .getExternalStorageDirectory(),
                            "Journal" + Utils.getID() + ".jpeg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photo));
                    mSelectedFilePath = photo.getAbsolutePath();
                    startActivityForResult(intent,
                            Utils.ACTION_TAKE_PICTURE);
                }
            } else {
                Toast.makeText(CreateContact.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_WRITE_PERMISSION) {
            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to

                new imageRotateAsync().execute();
            } else {
                Toast.makeText(CreateContact.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_READ_PERMISSION) {

            if (grantResults.length == 1
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We can now safely use the API we requested access to
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(
                            Intent.createChooser(intent, "Select Image"),
                            Utils.ACTION_GALLERY);

                }
            } else {
                Toast.makeText(CreateContact.this, "Permission denied", Toast.LENGTH_SHORT).show();
            }

        }
    }
}
