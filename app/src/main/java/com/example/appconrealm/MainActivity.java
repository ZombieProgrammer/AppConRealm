package com.example.appconrealm;

import android.content.Context;
import android.content.Intent;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.SyncConfiguration;
import io.realm.SyncCredentials;
import io.realm.SyncUser;
import io.realm.exceptions.RealmException;

import static io.realm.internal.SyncObjectServerFacade.getApplicationContext;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public static ListView listView;
    FloatingActionButton item_create, item_filter;
    FloatingActionMenu floatingActionMenu;
    Button button, clearFilter,cerrarPopup;
    Realm realm;
    public static ListPersonAdapter listPersonAdapter;
    int listViewPos;
    RealmResults<Persona> personas;
    //public static String id;
    public static Context context_;
    public static Intent intentViewPerson;
    SeekBar seekbar, seekbar2;
    TextView textv1, textv2;
    EditText edit1, edit2;

    // Popup
    private LayoutInflater layoutInflater;
    private View popupView;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        realm = Realm.getDefaultInstance(); // opens "myrealm.realm"
        context_ = getApplicationContext();


        /*realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 1; i<7;i++) {
                    Persona p = new Persona(i,i,i+"",i+"",i+"",i, "F");
                    realm.copyToRealm(p);
                }
                Persona p1 = new Persona(41,208,"123123", "Aleix","Aragon",72,"M");
                Persona p2 = new Persona(52,209,"456465","Brian","Adalid",24, "F");
                Persona p3 = new Persona(63,210,"78998","ZetroX","zombieassasin7",99, "M");
                realm.copyToRealm(p1);
                realm.copyToRealm(p2);
                realm.copyToRealm(p3);
            }
        });*/
        findViewsByIds();
        setOnClicks();


        final RealmResults<Persona> personas = realm.where(Persona.class).findAll();
        listPersonAdapter = new ListPersonAdapter(personas);
        if(personas.size()>0) listView.setAdapter(listPersonAdapter);
        listPersonAdapter.notifyDataSetChanged();

        listView.setClickable(true);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                System.out.println(adapterView.getSelectedView().getId() + " LALALALALA " + i);
                //Intent intent = new Intent(Activity.this,destinationActivity.class);
                //based on item add info to intent
//                startActivity(intent);
            }


        });

        /* try {

            *//*final Persona p = new Persona(1,"qwer","asd","asd","asd","asd");
            realm.beginTransaction();
            realm.copyToRealm(p); // This will do a deep copy of everything
            realm.commitTransaction();*//*

        }catch (RealmException r){
            r.printStackTrace();
        }finally {
            realm.close();
        }*/

    }
    void findViewsByIds(){
        floatingActionMenu = findViewById(R.id.fab);
        listView = findViewById(R.id.listView);
        item_create = findViewById(R.id.item_create);
        item_filter = findViewById(R.id.item_Filter);
        clearFilter = findViewById(R.id.clearFilter);
    }

    public void setOnClicks() {
//        floatingActionButton.setOnClickListener(this);
        item_create.setOnClickListener(this);
        item_filter.setOnClickListener(this);
        clearFilter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if ( v == item_create ) {
            floatingActionMenu.close(true);
            startActivity(new Intent(this, AddPersonActivity.class));
        } else if (v == item_filter){
            floatingActionMenu.close(true);
            abrirPopUp();
            //startActivity(new Intent(MainActivity.this, FilterActivity.class));
        } else if (v == clearFilter){
            final RealmResults<Persona> personas = realm.where(Persona.class).findAll();
            listPersonAdapter = new ListPersonAdapter(personas);
            if(personas.size()>0) listView.setAdapter(listPersonAdapter);
            listPersonAdapter.notifyDataSetChanged();
        }
    }

    void abrirPopUp(){
        /*final RealmResults<Persona> personas = realm.where(Persona.class).between("edad",20,80).findAll();
        listPersonAdapter = new ListPersonAdapter(personas);
        if(personas.size()>0) listView.setAdapter(listPersonAdapter);
        listPersonAdapter.notifyDataSetChanged();*/
//        button.setEnabled(false);
        layoutInflater =(LayoutInflater)getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = layoutInflater.inflate(R.layout.popup, null);
        popupWindow = new PopupWindow(popupView, RadioGroup.LayoutParams.MATCH_PARENT,
                RadioGroup.LayoutParams.WRAP_CONTENT);

        opciones();

//        cerrarPopup = popupView.findViewById(R.id.cerrarPopup);
        /*cerrarPopup.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                button.setEnabled(true);
            }
        });*/


        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.showAtLocation(listView, Gravity.CENTER, 0, 0);
    }

    void opciones() {

        textv1 = popupView.findViewById(R.id.textv1);
        textv2 = popupView.findViewById(R.id.textv2);
        seekbar = (SeekBar) popupView.findViewById(R.id.seekBar);
        seekbar2 = (SeekBar) popupView.findViewById(R.id.seekBar2);
        edit1 = popupView.findViewById(R.id.edit1);
        edit2 = popupView.findViewById(R.id.edit2);

        textv1.setText(0 + "");
        textv2.setText(9999 + "");

        edit2.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (s.length()==0){

                    final RealmResults<Persona> personas = realm.where(Persona.class).findAll();
                    listPersonAdapter = new ListPersonAdapter(personas);
                    if (personas.size() > 0) listView.setAdapter(listPersonAdapter);
                    listPersonAdapter.notifyDataSetChanged();

                }else {

                        final RealmResults<Persona> personas = realm.where(Persona.class).contains("genero",edit2.getText().toString()).findAll();
                        listPersonAdapter = new ListPersonAdapter(personas);
                        if(personas.size()>0) listView.setAdapter(listPersonAdapter);
                        listPersonAdapter.notifyDataSetChanged();

                    System.out.println(s.length());
                }
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textv1.setText("" + progress);
                final RealmResults<Persona> personas = realm.where(Persona.class).between("edad",Integer.parseInt(textv1.getText().toString()), Integer.parseInt(textv2.getText().toString())).findAll();
                listPersonAdapter = new ListPersonAdapter(personas);
                if(personas.size()>0) listView.setAdapter(listPersonAdapter);
                listPersonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekbar2.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textv2.setText("" + progress);
                final RealmResults<Persona> personas = realm.where(Persona.class).between("edad",Integer.parseInt(textv1.getText().toString()), Integer.parseInt(textv2.getText().toString())).findAll();
                listPersonAdapter = new ListPersonAdapter(personas);
                if(personas.size()>0) listView.setAdapter(listPersonAdapter);
                listPersonAdapter.notifyDataSetChanged();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        edit1.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {

                if (s.length()==0){

                    final RealmResults<Persona> personas = realm.where(Persona.class).findAll();
                    listPersonAdapter = new ListPersonAdapter(personas);
                    if (personas.size() > 0) listView.setAdapter(listPersonAdapter);
                    listPersonAdapter.notifyDataSetChanged();

                }else {

                    try {
                        int num = Integer.parseInt(edit1.getText().toString());
                        Log.i("",num+" is a number");
                        final RealmResults<Persona> personas = realm.where(Persona.class).equalTo("edad",Integer.parseInt(edit1.getText().toString())).findAll();
                        listPersonAdapter = new ListPersonAdapter(personas);
                        if(personas.size()>0) listView.setAdapter(listPersonAdapter);
                        listPersonAdapter.notifyDataSetChanged();
                    } catch (NumberFormatException e) {
                        Toast.makeText(getApplicationContext(),"Introduce un numero",Toast.LENGTH_SHORT).show();
                    }



                    System.out.println(s.length());
                }
            }


            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });



    }



}
