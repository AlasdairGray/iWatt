package uk.ac.hw.macs.nl148.iwatt;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Author: Neio Lucas
 * File : SecondFragment.java
 * Platform : Android Operating System
 * Date:  25/02/2016.
 * Description: This fragment displays the users personal information. wllowing him/her to edit it
 * if they wish.
 */

public class SecondFragment extends Fragment implements View.OnClickListener{

    View view;

    EditText name;
    EditText surname;
    EditText username;
    EditText email;
    TextView heading;
    Button exit;
    Button update;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.second_layout, container,false);

        name = (EditText) view.findViewById(R.id.update_name);
        surname = (EditText) view.findViewById(R.id.update_surname);
        username = (EditText) view.findViewById(R.id.update_username);
        heading = (TextView) view.findViewById(R.id.up_heading);
        email = (EditText) view.findViewById(R.id.update_email);

        exit = (Button) view.findViewById(R.id.exit_student_update);
        update = (Button) view.findViewById(R.id.update_student);


        DBHelper dbHelper = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class);
        RuntimeExceptionDao<Student, Object> studentDao = dbHelper.getStudentExceptionDao();
        List<Student> student = studentDao.queryForAll();

        Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "Simple tfb.ttf");

        name.setText(student.get(0).getName());
        surname.setText(student.get(0).getSurname());
        username.setText(student.get(0).getUsername());
        email.setText(student.get(0).getEmail());

        name.setTypeface(tf);
        surname.setTypeface(tf);
        username.setTypeface(tf);
        email.setTypeface(tf);
        heading.setTypeface(tf);
        exit.setTypeface(tf);
        update.setTypeface(tf);

        OpenHelperManager.releaseHelper();

        exit.setOnClickListener(this);
        update.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {

        if(exit == v)
        {
            //Intent close = new Intent(getActivity(),MainActivity.class);
            //getActivity().finish();
            //startActivity(close);
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
        }

        if(update == v )
        {
            DBHelper dbHelper = OpenHelperManager.getHelper(getActivity().getApplicationContext(), DBHelper.class);
            RuntimeExceptionDao<Student, Object> studentDao = dbHelper.getStudentExceptionDao();

            final UpdateBuilder<Student, Object> updateBuilder = studentDao.updateBuilder();
            List<Student> students = studentDao.queryForAll();
            // set the criteria like you would a QueryBuilder
            try {
                updateBuilder.where().eq("name", students.get(0).getName());
            } catch (SQLException e) {
                e.printStackTrace();
            }
            // update the value of your field(s)
            try {
                updateBuilder.updateColumnValue("name" /* column */, name.getText().toString() /* value */);
                updateBuilder.updateColumnValue("surname" /* column */, surname.getText().toString() /* value */);
                updateBuilder.updateColumnValue("username" /* column */, username.getText().toString() /* value */);
                updateBuilder.updateColumnValue("email" /* column */, email.getText().toString() /* value */);

            } catch (SQLException e) {
                e.printStackTrace();
            }
           

            if(students.get(0).getName().contentEquals(name.getText().toString()) && students.get(0).getSurname().contentEquals(surname.getText().toString())
                    && students.get(0).getUsername().contentEquals(username.getText().toString()) && students.get(0).getEmail().contentEquals(email.getText().toString()))
            {
                Toast.makeText(getActivity(), "No changes to update.", Toast.LENGTH_SHORT).show();
            }

            else {


                if (!email.getText().toString().contains("@") || !email.getText().toString().contains(".")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    TextView tx = new TextView(getActivity());
                    builder.setTitle("Error");
                    builder.setMessage("\n" +
                            "\nEmail address is invalid.\n");
                    builder.setView(tx);
                    builder.setPositiveButton("OKAY", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            AlertDialog build = builder.create();
                            build.cancel();

                        }

                    });

                    AlertDialog build = builder.create();
                    build.setCanceledOnTouchOutside(false);
                    build.show();
                } else {


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    TextView tx = new TextView(getActivity());
                    builder.setTitle("Confirm");
                    builder.setMessage("\n" +
                            "Are you sure you want to save?");
                    builder.setView(tx);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            AlertDialog build = builder.create();
                            try {
                                updateBuilder.update();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            build.cancel();
                            Toast.makeText(getActivity(), "Personal Information updated!", Toast.LENGTH_SHORT).show();
                            OpenHelperManager.releaseHelper();
                            //List<Student> local = studentDao.queryForAll();
                            //Log.d("bean", local.toString());
                            Intent i = new Intent(getActivity(), MainActivity.class);
                            getActivity().finish();
                            startActivity(i);

                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {


                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            AlertDialog build = builder.create();
                            build.cancel();

                        }
                    });

                    AlertDialog build = builder.create();
                    build.setCanceledOnTouchOutside(false);
                    build.show();

                }
            }
        }

    }
}
