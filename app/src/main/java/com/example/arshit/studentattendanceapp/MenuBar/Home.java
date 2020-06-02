//package com.example.arshit.studentattendanceapp.MenuBar;
//
//import android.app.Activity;
//
//import android.app.ActionBar;
//import android.app.Fragment;
//import android.app.FragmentManager;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.SharedPreferences;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AlertDialog;
//import android.support.v7.app.AppCompatActivity;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.support.v4.widget.DrawerLayout;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.example.arshit.studentattendanceapp.Fragment.HomeFragment;
//import com.example.arshit.studentattendanceapp.Login.SignIn;
//import com.example.arshit.studentattendanceapp.Model.Category;
//import com.example.arshit.studentattendanceapp.MenuBar.PractiseActivity;
//import com.example.arshit.studentattendanceapp.R;
//import android.content.Intent;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import android.support.design.widget.FloatingActionButton;
//import android.support.design.widget.Snackbar;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.support.design.widget.NavigationView;
//import android.support.v4.view.GravityCompat;
//import android.support.v4.widget.DrawerLayout;
//import android.support.v7.app.ActionBarDrawerToggle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.Toolbar;
//import android.view.Menu;
//import android.view.MenuItem;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.arshit.studentattendanceapp.ViewHolder.MenuViewHolder;
//import com.firebase.ui.database.FirebaseRecyclerAdapter;
//import com.google.android.gms.common.internal.service.Common;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.squareup.picasso.Picasso;
//
//import java.util.HashMap;
//
//import java.util.UUID;
//import java.util.zip.CRC32;
//
//public class Home extends AppCompatActivity
//        implements NavigationView.OnNavigationItemSelectedListener {
//
//    FirebaseDatabase database;
//    DatabaseReference category;
//    TextView txtFullName;
//    RecyclerView recycler_menu;
//    Category newCategory;
//
//    RecyclerView.LayoutManager layoutManager;
//    private DatabaseReference Rootref;
//    String d;
//    AlertDialog.Builder builder;
//    HashMap<String,String> hashMap,hashMap1;
//    FirebaseAuth mAuth;
//    FirebaseRecyclerAdapter adapter;
//    FirebaseUser firebaseUser;
//    private String currentUserId;
//    EditText txt_category;
//    Button btnSelect,btnUpload;
//    private static int Gallery_Pick = 1;
//    private ProgressDialog mProgressDialog;
//
//       String id;
//    private StorageReference imgStorageReference;
//
//
//
//    Uri saveUri;
//    RecyclerView recycler_category;
//    String subcatdiscount,subcatName,subcatdescription,subcatprice,categoryName;
//    Button btn_category,btn_subcat;
//    String catName;
//    public static final String UPDATE = "UPDATE";
//    public static final String DELETE = "DELETE";
//    View view;
//    View custom_layout;
//    String catTxt;
//    LayoutInflater inflater;
//
//    String downloadUrl;
//    SharedPreferences pref;
//    SharedPreferences.Editor editor;
//    TextView txt;
//    AlertDialog alertDialog;
//    EditText editText;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_home2);
//
//        btnSelect = findViewById(R.id.btnSelect);
//        btnUpload = findViewById(R.id.btnUpload);
//
//
//btnSelect.setOnClickListener(new View.OnClickListener() {
//    @Override
//    public void onClick(View view) {
//
//
//        editText = findViewById(R.id.edit_text_cat);
//        catTxt = editText.getText().toString();
//
//
//        pref = getApplicationContext().getSharedPreferences("MyPref", 0); // 0 - for private mode
//        editor = pref.edit();
//        editor.putString("key_name",catTxt);
//
//
//        editor.commit();
//        // Storing string
//        String e = pref.getString("key_name", null);
//
//        Toast.makeText(Home.this, "catxxxxxxxxxx =>"+e, Toast.LENGTH_SHORT).show();
//
//
//        chooseImage();
//
//    }
//});
//
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
//        toolbar.setTitle("Menu");
//
//        //Init Firebase
//        database = FirebaseDatabase.getInstance();
//        category = database.getReference("Category");
//
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                Intent cartIntent = new Intent(Home.this,PractiseActivity.class);
//
////                startActivity(cartIntent);
//
//
//                showDialog();
//
//            }
//        });
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout1);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//
//        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view1);
//        navigationView.setNavigationItemSelectedListener(this);
//
//        //Set Name for user
//        View headerView = navigationView.getHeaderView(0);
//        txtFullName = headerView.findViewById(R.id.txtFullName);
//        txtFullName.setText("Arshit Jain");
//
//        imgStorageReference = FirebaseStorage.getInstance().getReference();
//
//
//        //Load menu
//        recycler_menu = findViewById(R.id.recycler_menu);
//        recycler_menu.setHasFixedSize(true);
//        layoutManager = new LinearLayoutManager(this);
//        recycler_menu.setLayoutManager(layoutManager);
//
////        loadMenu();
//
//    }
//
////    private void loadMenu() {
////
////        adapter = new FirebaseRecyclerAdapter<Category,MenuViewHolder>(Category.class,R.layout.menu_item,MenuViewHolder.class,category) {
////
////            @NonNull
////            @Override
////            public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
////                return null;
////            }
////
////            @Override
////            protected void onBindViewHolder(@NonNull MenuViewHolder holder, int position, @NonNull Category model) {
////
////
////
////            }
////
////            @Override
////            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
////                viewHolder.txtMenuName.setText(model.getCategoryName());
////                Picasso.get().load(model.getCategoryImage()).into(viewHolder.imageView);
////                final Category clickItem = model;
////                viewHolder.setItemClickListener(new MenuViewHolder.ItemClickListener() {
////                    @Override
////                    public void onClick(View view, int position, boolean isLongClick) {
////                        //Get CategoryId and send to new Activity
////                        Intent foodList = new Intent(Home.this, FoodList.class);
////                        //Because CategoryId is key, so we just get the key of this item
////                        foodList.putExtra("CategoryId", adapter.getRef(position).getKey());
////                        startActivity(foodList);
////                    }
////                });
////            }
////        };
////        recycler_menu.setAdapter(adapter);
////    }
//
//    @Override
//    public void onBackPressed() {
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        if (drawer.isDrawerOpen(GravityCompat.START)) {
//            drawer.closeDrawer(GravityCompat.START);
//        } else {
//            super.onBackPressed();
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    @SuppressWarnings("StatementWithEmptyBody")
//    @Override
//    public boolean onNavigationItemSelected(MenuItem item) {
//        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_menu) {
//            return true;
//        }else if(id == R.id.nav_cart){
//            Intent cartIntent = new Intent(Home.this,PractiseActivity.class);
//            startActivity(cartIntent);
//
//        }else if(id == R.id.nav_orders){
//            Intent orderIntent = new Intent(Home.this,PractiseActivity.class);
//            startActivity(orderIntent);
//
//        }else if(id == R.id.nav_log_out){
//            Intent signIn = new Intent(Home.this, SignIn.class);
//            signIn.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(signIn);
//        }
//
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
//
//
//    private void showDialog() {
//
//
//          builder = new AlertDialog.Builder(Home.this);
//        builder.setTitle("Enter Category Name : ");
//        builder.setMessage("Please fill full information");
//
//          inflater = this.getLayoutInflater();
//
//      custom_layout = inflater.inflate(R.layout.custom_dialog_category,null);
//
//
//
//
//
//        builder.setView(custom_layout);
//
//        builder.setIcon(R.drawable.ic_shopping_cart_black_24dp);
//
//
//
//        btnSelect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Toast.makeText(Home.this, "cat ==> "+catTxt, Toast.LENGTH_SHORT).show();
//
//                if (alertDialog != null && alertDialog.isShowing()) {
//
//
//                    chooseImage();
//
//                    alertDialog.dismiss();
//                }
//
//
//
//
//
//            }
//
//        });
//
//
//        btnUpload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////
//                if (alertDialog != null && alertDialog.isShowing()) {
//
////                    uploadImage();
//
//                    alertDialog.dismiss();
//                }
//
//
//            }
//        });
//
//
//        builder.setPositiveButton("YES ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                if (newCategory !=null){
//
//                    category.push().setValue(newCategory);
//                }
//
//
//                dialogInterface.dismiss();
//            }
//        });
//
//
//        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                dialogInterface.dismiss();
//            }
//        });
//
//       alertDialog = builder.create();
//        alertDialog.show();
//    }
//
//    private void uploadImage() {
//
//        String e = pref.getString("key_name", null);
//
//        Toast.makeText(this, "catxxxxxxxxxx =>"+e, Toast.LENGTH_SHORT).show();
//
//
//
//        if (saveUri!=null){
//
//            mProgressDialog = new ProgressDialog(this);
//            mProgressDialog.setTitle("Saving Changes");
//            mProgressDialog.setMessage("Wait untill changes are saved");
//            mProgressDialog.setCanceledOnTouchOutside(false);
//
//
//            final String imageName = UUID.randomUUID().toString();
//
//            final StorageReference filepath = imgStorageReference.child("Category_images/"+imageName);
//
//
//            filepath.putFile(saveUri)
//                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            mProgressDialog.dismiss();
//
////                            Toast.makeText(Home.this, "cat"+catTxt, Toast.LENGTH_SHORT).show();
//
//                            Toast.makeText(Home.this, "Uploaded !! ", Toast.LENGTH_SHORT).show();
//
//
//                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                @Override
//                                public void onSuccess(Uri uri) {
//
//
//                                    newCategory = new Category(catTxt,uri.toString());
//
//                                    Toast.makeText(Home.this, "workdone", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//
//                        }
//
//
//
//                    })
//
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            mProgressDialog.dismiss();
//
//                            Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//
//                        }
//                    })
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            double progres = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
//
//                            mProgressDialog.setMessage("Uploaded"+progres+"%");
//
//                        }
//                    });
//
//
//
//        }
//    }
//
//
//    private void chooseImage(){
//
//        String e = pref.getString("key_name", null);
//
//        Toast.makeText(this, "catxxxxxxxxxx =>"+e, Toast.LENGTH_SHORT).show();
//
//        Intent galleryIntent = new Intent();
//
//        galleryIntent.setType("image/*");
//        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
//
//
//        startActivityForResult(galleryIntent,Gallery_Pick);
//
//        Toast.makeText(this, "gallery 2"+catTxt, Toast.LENGTH_SHORT).show();
//
//    }
//
//
//
////    @Override
////    public void onPause() {
////        super.onPause();
////        if (mProgressDialog != null) {
////            mProgressDialog.dismiss();
////            mProgressDialog = null;
////        }
////    }
////
////    @Override
////    public void onDestroy() {
////        super.onDestroy();
////        if (mProgressDialog != null) {
////            mProgressDialog.dismiss();
////            mProgressDialog = null;
////        }
////    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//
//
//        if (requestCode == Gallery_Pick && resultCode == RESULT_OK && data!=null && data.getData() != null) {
//
//            Toast.makeText(this, "gallery 2 ===>"+catTxt, Toast.LENGTH_SHORT).show();
//
//            saveUri = data.getData();
//
//
//uploadImage();
//        }
////            btnSelect.setText("Image Selected");
//
//
////            CropImage.activity(imageURI).setAspectRatio(1, 1).start(getContext(),HomeFragment.this);
//
//
////        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
////
////            CropImage.ActivityResult result = CropImage.getActivityResult(data);
////
////
////            if (resultCode == Activity.RESULT_OK) {
////
////
////
////
////
////                mProgressDialog.show();
////
////
////                Uri resultUri = result.getUri();
////
////
////
////                final String imageName = UUID.randomUUID().toString();
////
////                StorageReference filepath = imgStorageReference.child("Category_images").child(imageName + ".jpg");
////
////                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
////                    @Override
////                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
////
////                        if (task.isSuccessful()) {
////
//// imgStorageReference.child("Category_images").child(imageName+ ".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
////                                @Override
////                                public void onSuccess(Uri uri) {
////
////                                    Toast.makeText(getContext(), "id", Toast.LENGTH_SHORT).show();
////
////
////
////                                    downloadUrl = uri.toString();
//////
////
////
////
//////                                    Toast.makeText(getContext(), "sss"+catTxt, Toast.LENGTH_SHORT).show();
////
//////                                    method1(downloadUrl);
////
//////
//////                                    hashMap.put("Category",catTxt);
//////                                    hashMap.put("imageURL",downloadUrl);
////
////
////
////
////
////
////
////
////
////
////
//////                                    HashMap<String,String> SubcatHashMap = new HashMap<>();
//////
//////                                    hashMap.put("Food Name",subcatName);
//////                                    hashMap.put("Description",subcatdescription);
//////                                    hashMap.put("Price",subcatprice);
//////                                    hashMap.put("Discount",subcatdiscount);
//////                                    hashMap.put("imageURL",downloadUrl);
//////                                    mRootRef.child("Category").push().setValue(SubcatHashMap);
////
////
////
////                                    hashMap.put("Image",downloadUrl);
////
////
////onResume();
////                                    method();
////
////                                    mProgressDialog.dismiss();
////
//////                                    Toast.makeText(getContext(), txt_category. getText().toString()+" Category Created ", Toast.LENGTH_SHORT).show();
////
////                                }
////
////                            });
////
////                        }
////
////                        else {
////
////                            Toast.makeText(getContext(), "Not Working", Toast.LENGTH_SHORT).show();
////                            mProgressDialog.dismiss();
////                        }
////
////                    }
////                });
////            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
////
////                Exception error = result.getError();
////            }
////
////        }
//    }
//
//
//
//}