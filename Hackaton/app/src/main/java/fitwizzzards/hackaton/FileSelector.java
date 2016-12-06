package fitwizzzards.hackaton;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.view.View;

import android.widget.ArrayAdapter;

import android.widget.ListView;

import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileSelector extends ListActivity {



    private List<String> item = null;

    private List<String> path = null;

    private ArrayList<String> filesToUpload;

    private String root="/";

    private TextView myPath;

    private final int READ_EXTERNAL_STORAGE_PERMISSION_CODE = 51;



    /** Called when the activity is first created. */

    @Override

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_file_selector);

        myPath = (TextView)findViewById(R.id.path);

        filesToUpload = new ArrayList<>();

        getDir(root);

        Intent dropboxUpload = new Intent(this, FileSelector.class);
        //getPermission(root);

    }

    @TargetApi(23)
    private void getPermission(String dirPath){
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            //ask for permission
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, READ_EXTERNAL_STORAGE_PERMISSION_CODE);
        }else
            getDir(root);
    }


    private void getDir(String dirPath)

    {

        myPath.setText("Location: " + dirPath);


        item = new ArrayList<String>();
        path = new ArrayList<String>();
        File f = new File(dirPath);
        File[] files = f.listFiles();

        if(!dirPath.equals(root))
        {
            item.add(root);
            path.add(root);
            item.add("../");
            path.add(f.getParent());
        }

        for(int i=0; i < files.length; i++)
        {

            File file = files[i];

            path.add(file.getPath());

            if(file.isDirectory())

                item.add(file.getName() + "/");

            else

                item.add(file.getName());

        }

        ArrayAdapter<String> fileList =

                new ArrayAdapter<String>(this, R.layout.row, item);

        setListAdapter(fileList);

    }



    @Override

    protected void onListItemClick(ListView l, View v, int position, long id) {



        File file = new File(path.get(position));



        if (file.isDirectory())

        {

            if(file.canRead())

                getDir(path.get(position));

            else

            {



                new AlertDialog.Builder(this)

                        .setTitle("[" + file.getName() + "] folder can't be read!")

                        .setPositiveButton("OK",

                                new DialogInterface.OnClickListener() {



                                    @Override

                                    public void onClick(DialogInterface dialog, int which) {

                                        // TODO Auto-generated method stub

                                    }

                                }).show();


            }

        }

        else

        {

            final String fileAddress = file.getPath() + "/"+ file.getName();
            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which){
                        case DialogInterface.BUTTON_POSITIVE:
                            //Yes button clicked
                            filesToUpload.add(fileAddress);
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            //No button clicked
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to upload " + file.getName() + "?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();



        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case READ_EXTERNAL_STORAGE_PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    getDir(root);
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

}

