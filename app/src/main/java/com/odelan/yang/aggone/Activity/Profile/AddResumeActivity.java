package com.odelan.yang.aggone.Activity.Profile;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.github.pdfviewer.PDFView;
import com.odelan.yang.aggone.Activity.BaseActivity;
import com.odelan.yang.aggone.Adapter.Profile.ResumeAdapter;
import com.odelan.yang.aggone.Dialog.DeleteDialog;
import com.odelan.yang.aggone.Model.Resume;
import com.odelan.yang.aggone.Model.User;
import com.odelan.yang.aggone.R;
import com.odelan.yang.aggone.Utils.API;
import com.odelan.yang.aggone.Utils.APICallback;
import com.odelan.yang.aggone.Utils.AppData;
import com.odelan.yang.aggone.Utils.Constants;
import com.odelan.yang.aggone.Utils.PathUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.odelan.yang.aggone.Activity.Profile.ProfileActivity.REQUEST_STORAGE_WRITE_PERMISSION;
import static com.odelan.yang.aggone.Utils.API.baseUrl;
import static com.odelan.yang.aggone.Utils.API.resumeDirUrl;

public class AddResumeActivity extends BaseActivity {
    public final static int PICK_PDF_CODE = 4567;

    private User user;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refresh_layout;
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;
    @BindView(R.id.resume_title)
    TextView    txt_title;

    ResumeAdapter resumeAdapter;
    List<Resume> resumes = new ArrayList<>();
    int selected_index = 0;

    ResumeAdapter.EventListener resumeListener = new ResumeAdapter.EventListener() {
        @Override
        public void onAddResume(){
            uploadResume();
        }
        @Override
        public void onClickItem(int index) {
            selected_index = index;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE_PERMISSION);
            } else {
                downloadResume(resumes.get(index).resume_url);
            }
        }
        @Override
        public void onLongClickItem(final int index) {
            DeleteDialog dialog = new DeleteDialog(AddResumeActivity.this);
            dialog.setListener(() -> API.deleteResume(AddResumeActivity.this, resumes.get(index).id, new APICallback<Boolean>() {
                @Override
                public void onSuccess(Boolean response) {
                    resumes.remove(index);
                    resumeAdapter.setDataList(resumes);
                }
                @Override
                public void onFailure(String error) {
                    Toast.makeText(AddResumeActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            }));
            View decorView = dialog.getWindow().getDecorView();
            decorView.setBackgroundResource(android.R.color.transparent);
            dialog.show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_resume);

        ButterKnife.bind(this);
        user = getIntent().getExtras().getParcelable(Constants.USER);
        setActivity();
    }

    void uploadResume(){
        requestPermission();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_STORAGE_WRITE_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (AppData.user.id.equals(user.id)){
                choosePdf();
            } else {
                if (selected_index < resumes.size()) {
                    downloadResume(resumes.get(selected_index).resume_url);
                }
            }

        }
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_STORAGE_WRITE_PERMISSION);
        } else {
            choosePdf();
        }
    }

    private void choosePdf(){
        Intent intentPDF = new Intent(Intent.ACTION_GET_CONTENT);
        intentPDF.setType("application/pdf");
        intentPDF.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intentPDF , "Select Pdf file"), PICK_PDF_CODE);
    }

    void setActivity(){
        if (user.id.equals(AppData.user.id)){
            txt_title.setVisibility(View.VISIBLE);
        } else{
            txt_title.setVisibility(View.GONE);
        }

        resumeAdapter = new ResumeAdapter(this, resumes, resumeListener);
        resumeAdapter.user = user;
        recycler_view.setLayoutManager(new GridLayoutManager(this, 2));
        recycler_view.setAdapter(resumeAdapter);

        refresh_layout.setOnRefreshListener(refreshListener);
        refresh_layout.autoRefresh();

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
    }

    private void downloadResume(String filename){
        String foldername = "Aggone/resume";
        String dirPath = Environment.getExternalStorageDirectory() + "/" + foldername;
        File folder = new File(dirPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        final String filepath = folder.getAbsolutePath() + "/" + filename;
        showProgress();
        AndroidNetworking.download(baseUrl + resumeDirUrl + filename, dirPath, filename)
                .setPriority(Priority.HIGH)
                .build()
                .setDownloadProgressListener(new DownloadProgressListener() {
                    @Override
                    public void onProgress(long bytesDownloaded, long totalBytes) {
                        //Log.d("LOGO_PROGRESS", "Downloaded: "+bytesDownloaded+"/"+totalBytes);
                    }
                })
                .startDownload(new DownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        dismissProgress();
                        Toast.makeText(AddResumeActivity.this, "Download resume file : " + filepath, Toast.LENGTH_SHORT).show();
                        openPdfFile(filepath);
                    }

                    @Override
                    public void onError(ANError anError) {
                        //Log.e("LOGO_ERROR",anError.getErrorCode()+"    "+ anError.getErrorDetail());
                        dismissProgress();
                        Toast.makeText(AddResumeActivity.this, "Failed Download", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    OnRefreshListener refreshListener = new OnRefreshListener() {
        @Override
        public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
            API.getAllResumes(AddResumeActivity.this, user.id, new APICallback<List<Resume>>() {
                @Override
                public void onSuccess(List<Resume> response) {
                    refresh_layout.finishRefresh();
                    resumes.clear();
                    resumes.addAll(response);
                    resumeAdapter.setDataList(resumes);
                }
                @Override
                public void onFailure(String error) {
                    refresh_layout.finishRefresh();
                }
            });
        }
    };

    @OnClick(R.id.btn_back) void onClickBack(){
        finish();
    }

    private void uploadResumeFile(String filepath){
        showProgress();
//        String filename=filepath.substring(filepath.lastIndexOf("/")+1);
        String filename = user.username + "_" +System.currentTimeMillis()/1000 + ".pdf";
        File pdffile = new File(filepath);
        API.uploadResume(this, filename, pdffile, new APICallback<String>() {
            @Override
            public void onSuccess(String response) {
                dismissProgress();
                refresh_layout.autoRefresh();
            }

            @Override
            public void onFailure(String error) {
                dismissProgress();
                Toast.makeText(AddResumeActivity.this, "Try to upload pdf file again", Toast.LENGTH_SHORT).show();
            }
        });
    }

    void openPdfFile(String filename){
        File file = new File(filename); // Here you declare your pdf path
        if (!file.exists()){
            return;
        }
        PDFView.with(this)
                .setfilepath(filename)
                //.setSwipeOrientation() //if false pageswipe is vertical otherwise its horizontal
                .start();
//        Intent pdfViewIntent = new Intent(Intent.ACTION_VIEW);
//        //pdfViewIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        pdfViewIntent.setDataAndType(Uri.fromFile(file),"application/pdf");
//        pdfViewIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
////        startActivity(pdfViewIntent);
//        Intent intent = Intent.createChooser(pdfViewIntent, "Open File");
//        try {
//            startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            // Instruct the user to install a PDF reader here, or something
//        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case PICK_PDF_CODE:
                if (resultCode == RESULT_OK) {
                    // Get the Uri of the selected file
                    Uri uri = data.getData();
//                    String uriString = uri.toString();
//                    showToast(uriString);
//                    try{
                        String uriString = PathUtil.getPath_new(this, uri);
                        if(uriString != null){
//                            showToast(uriString);
                            uploadResumeFile(uriString);
                        } else {
                            showToast("Please choose resume file again.");
                        }
//                    } catch (URISyntaxException e){
//                    }

                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
