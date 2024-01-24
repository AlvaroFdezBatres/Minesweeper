package com.example.buscaminas;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends AppCompatActivity{
    Random r = new Random();
    int filas;
    int columnas;
    ConstraintLayout constraintLayout;
    GridLayout tablero;
    int contador;
    int numeroMinas;
    int minasRestantes;
    int minaSeleccionada;
    TextView nMinas;

//    Button [][] botones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        constraintLayout = findViewById(R.id.constraintLayout);
        tablero = findViewById(R.id.tablero);

        constraintLayout.post(new Runnable() {
            @Override
            public void run() {
                filas = 8;
                columnas = 8;
                numeroMinas = 10;
                minaSeleccionada = R.drawable.bomba;
                inicializarTablero(filas, columnas, numeroMinas);
            }
        });
    }

/*    public void revelarMinas() {
     //Descubre todas las casillas que contengan minas
        int matriz[][] = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (matriz[i][j] == -1) {
                    View minaView = tablero.getChildAt(i * columnas + j);
                    if (minaView instanceof ImageButton) {
                        ((ImageButton) minaView).setBackgroundResource(R.drawable.bomba);
                    }
                }
            }
        }
    }*/
    private void desactivarEventos(){
        //TODO: Desactivo todos los eventos de botón del tablero
        for (int i = 0; i < tablero.getChildCount(); i++) {
            View child = tablero.getChildAt(i);
            child.setOnClickListener(null);
        }
    }

 /*   private void descubrirCasillas(int fila, int columna, Button b){
        //TODO: Si no hay minas alrededor de la casilla marcada, se descubren
        botones = new Button[filas][columnas];
        int [][] matriz = new int[filas][columnas];
        if(fila -1 >= 0 && fila +1 >= filas && columna -1 >= 0 && columna + 1 <= columnas){
            for(int i= fila -1 ; i<= fila +1; i++){
                for(int j= columna -1 ; j<= columna +1; j++){
                    if (i >= 0 && i < filas && j >= 0 && j < columnas) {
                        if (botones[i][j] instanceof Button && matriz[i][j] == 0) {
                            botones[i][j].setBackgroundResource(R.drawable.mi_color);
                        }
                    }
                }
            }
        }
    }*/

    private void inicializarTablero(int filas, int columnas, int numeroMinas) {
        tablero.removeAllViews();

        //Parámetros del gridLayout
        tablero.setRowCount(filas);
        tablero.setColumnCount(columnas);

        int [][] matriz = new int[filas][columnas];

        //Relleno el tablero
        for(int i=0; i<filas; i++){
            for(int j=0; j<columnas; j++){
                matriz[i][j]= 0;
            }
        }


        //Relleno con minas, mientras que no haya tantos -1s como minas indique la dificultad, se siguen añadiendo -1
        int minasColocadas = 0;
        while(minasColocadas < numeroMinas){
            int fila = r.nextInt(filas);
            int columna = r.nextInt(columnas);

            if (matriz[fila][columna] == 0) {
                matriz[fila][columna] = -1; //Se coloca una mina en la posición (fila, columna) random
                minasColocadas++;
            }
        }


        //Capturo el ancho y el alto del layout
        int width = constraintLayout.getWidth()/columnas;
        int height = constraintLayout.getHeight()/filas;
        //Calculo los parámetros que van a tener las casillas
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width,height);
        layoutParams.setMargins(0,0,0,0);



        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                //Capturo la fila y la columna que el usuario ha clickado
                final int fila = i;
                final int columna = j;

                //Si la posición es -1(mina), coloco un ImageButton
                if(matriz[i][j]==-1)
                {
                    ImageButton mina = new ImageButton(this);
                    mina.setBackgroundResource(R.drawable.border_button);
                    mina.setLayoutParams(layoutParams);
                    mina.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: Si haces click, explotarán todas las bombas
                            //revelarMinas();
                            MediaPlayer explosion = MediaPlayer.create(getApplicationContext(), R.raw.explosion);
                            explosion.start();
                            for (int i = 0; i < filas; i++) {
                                for (int j = 0; j < columnas; j++) {
                                    if (matriz[i][j] == -1) {
                                        View minaView = tablero.getChildAt(i * columnas + j);
                                        if (minaView instanceof ImageButton) {
                                            minaView.setBackgroundResource(minaSeleccionada);
                                        }
                                    }
                                }
                            }
                            desactivarEventos();
                            Toast.makeText(getApplicationContext(),"¡PERDISTE!", Toast.LENGTH_LONG).show();
                        }
                    });//fin onCLick

                    //Igualo las minasRestantes a las que tiene que descubrir para ganar
                    minasRestantes = numeroMinas;

                    mina.setOnLongClickListener(new View.OnLongClickListener() {

                        @Override
                        public boolean onLongClick(View v) {
                            //TODO: Si haces un click largo, marcarás la casilla con una bandera
                            ImageButton minaDesactivada = (ImageButton) v;
                            minaDesactivada.setBackgroundResource(R.drawable.bandera);
                            minaDesactivada.setOnClickListener(null);
                            minaDesactivada.setOnLongClickListener(null);
                            minasRestantes--;
                            if(minasRestantes == 0){
                                Toast.makeText(getApplicationContext(),"¡ENHORABUENA, HAS GANADO!",Toast.LENGTH_LONG).show();
                                MediaPlayer aplausos = MediaPlayer.create(getApplicationContext(), R.raw.aplausos);
                                aplausos.start();
                                desactivarEventos();
                            }else if(minasRestantes == 1) {
                                Toast.makeText(getApplicationContext(),"¡¡¡¡Ya casi lo tienes, te queda 1 mina!!!!"+minasRestantes,Toast.LENGTH_SHORT).show();

                            }else {
                                Toast.makeText(getApplicationContext(),"¡Bien, una mina menos! Te quedan "+minasRestantes,Toast.LENGTH_SHORT).show();
                            }
                            return true;
                        }
                    });//fin onLongClick
                    tablero.addView(mina);
                }
                //Si la posición es un 0(no mina), creo un botón
                else
                {
                    //Creo las casillas
                    Button casilla = new Button(this);
                    //Le añado el estilo a los botones que he diseñado en el xml
                    casilla.setBackgroundResource(R.drawable.border_button);
                    casilla.setText("");
                    //botones[i][j] = casilla;

                    casilla.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //TODO: Si haces click se descubren la casilla
                            Button casillaDescubierta = (Button) v;
                            int contadorMinas = 0;
                            //Controlo que no se salgan de los límites de filas y columnas
                            for(int i=fila-1; i<=fila+1; i++){
                                for(int j=columna-1; j<=columna+1; j++){
                                    if(i >= 0 && i < filas && j >= 0 && j < columnas){
                                        if(matriz[i][j] == -1){
                                            contadorMinas++;
                                        }
                                    }
                                }//for columnas
                            }//for filas

                            if(contadorMinas == 0){
                                casillaDescubierta.setText("");
                                //descubrirCasillas(fila, columna, casillaDescubierta);
                            }else{
                                casillaDescubierta.setText(contadorMinas+"");
                            }

                            casillaDescubierta.setBackgroundResource(R.drawable.mi_color);
                            casillaDescubierta.setOnClickListener(null);
                        }//fin on click
                    });
                    //le añado los parámetros a los botones
                    casilla.setLayoutParams(layoutParams);

                    casilla.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            //TODO: Si haces un click largo en una casilla, se descubren todas las minas y pierdes
                            MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.explosion);
                            mp.start();
                            //revelarMinas();
                            for (int i = 0; i < filas; i++) {
                                for (int j = 0; j < columnas; j++) {
                                    if (matriz[i][j] == -1) {
                                        View minaView = tablero.getChildAt(i * columnas + j);
                                        if (minaView instanceof ImageButton) {
                                            minaView.setBackgroundResource(minaSeleccionada);
                                        }
                                    }
                                }
                            }//fin for revelar minas
                            desactivarEventos();
                            Toast.makeText(getApplicationContext(), "YOU LOST!!!!!", Toast.LENGTH_LONG).show();
                            return true;
                        }
                    });
                    tablero.addView(casilla);
                }
            }//fin segundo for
        }//fin primer for
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //momento en el que se crea el menú de opciones
        getMenuInflater().inflate(R.menu.mi_menu, menu);

        return true; //mostrar los 3 puntitos de opciones
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.inst){
            crearInstrucciones();
        }else if(item.getItemId() == R.id.restart){
            reiniciarJuego();
        }else if(item.getItemId() == R.id.config){
            elegirDificultad();
        }else if(item.getItemId() == R.id.estiloBomba){
            elegirDiseñoBomba();

        }
        return super.onOptionsItemSelected(item);
    }

    private void elegirDiseñoBomba() {
        Dialog dialogBomba = new Dialog(this);
        // Inflar el diseño personalizado desde el archivo XML
        dialogBomba.setContentView(R.layout.mi_dialog_bomba);

        Spinner miSpinner = dialogBomba.findViewById(R.id.miSpinner);
        Mina[] misBombas = new Mina[6];
        misBombas[0] = new Mina("Bomba clásica", R.drawable.bomba);
        misBombas[1] = new Mina("Mina Bomber", R.drawable.bomba2);
        misBombas[2] = new Mina("Cóctel Molotov", R.drawable.coctel_molotov);
        misBombas[3] = new Mina("Dinamita", R.drawable.dinamita);
        misBombas[4] = new Mina("Granada", R.drawable.granada);
        misBombas[5] = new Mina("Mina Submarina", R.drawable.mina_submarina);


        miSpinner.setAdapter(new MinaAdapter(this, R.layout.mi_fila_spinner, misBombas));

        miSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                minaSeleccionada = misBombas[position].getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                minaSeleccionada = R.drawable.bomba;
            }
        });

        dialogBomba.show();
    }


    //Por cada uno de los datos que tiene rellena los datos en el layout
    public class MinaAdapter extends ArrayAdapter<Mina> {
        Mina [] misMinas;

        //constructor adaptador
        public MinaAdapter(@NonNull Context context, int resource, @NonNull Mina[] objects) {
            super(context, resource, objects);
            misMinas = objects;
        }

        //truco para flexibilidad de app -->
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return crearFila(position, convertView, parent);
        }

        @Override
        public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            return crearFila(position, convertView, parent);
        }

        //Va a rellenar el layout con los datos que le pasamos
        public View crearFila(int position, View convertView, ViewGroup parent){
            //Para programar el adpatador, crearFila va a ser invocado una vez por cada objeto del array de ciudades
            //1º Inflamos el xml con nuestra vista personalizada
            LayoutInflater miInflador = getLayoutInflater(); //Esto llega del contexto. Ya está creado y lo guardamos en esa variable
            View miFila = miInflador.inflate(R.layout.mi_fila_spinner, parent, false); //Vista que representa todos los objetos de mifila_ciudad

            //2º encontramos referencias a los objetos dec ada una de las filas infladas
            ImageView imageView = miFila.findViewById(R.id.imgBomba);
            TextView textView = miFila.findViewById(R.id.txtBomba);

            //3º Rellenar los datos, con el objeto i-ésimo del array de objetos (position)
            imageView.setImageResource(misMinas[position].getId());
            textView.setText(misMinas[position].getNombre());

            //retornar la fila "instanciada"
            return miFila;
        }
    }


    private void reiniciarJuego() {
        inicializarTablero(filas,columnas,numeroMinas);
    }

    private void crearInstrucciones() {
        Dialog dialog = new Dialog(this);

        // Inflar el diseño personalizado desde el archivo XML
        dialog.setContentView(R.layout.mi_dialog_instrucciones);


        TextView txtInstrucciones = dialog.findViewById(R.id.txtInstrucciones);
        //Añado el texto al textView ya creado en el dialog
        txtInstrucciones.setText("Cuando pulsas en una casilla, sale un " +
                "número que identifica cuántas minas hay " +
                "alrededor: Ten cuidado porque si pulsas en " +
                "una casilla que tenga una mina escondida, " +
                "perderás. Si crees o tienes la certeza de " +
                "que hay una mina, haz un click largo sobre " +
                "la casilla para señalarla. No hagas un click " +
                "largo en una casilla donde no hay una mina " +
                "porque perderás. Ganas una vez hayas " +
                "encontrado todas las minas.");

        Button btnOk = dialog.findViewById(R.id.btnOk);
        btnOk.setBackgroundColor(Color.WHITE);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:Cerrar el diálogo cuando se hace clic en "OK"
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public void elegirDificultad () {
        Dialog dialog = new Dialog(this);

        // Inflar el diseño personalizado desde el archivo XML
        dialog.setContentView(R.layout.mi_dialog_dificultad);

        // Creo los radiobuttons y los añado a un array para darles funcionalidad
        RadioButton[] radioButtons = new RadioButton[3];
        radioButtons[0] = dialog.findViewById(R.id.principiante);
        radioButtons[1] = dialog.findViewById(R.id.amateur);
        radioButtons[2] = dialog.findViewById(R.id.avanzado);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Según el botón pulsado, las filas serán unas u otras
                RadioButton rb = (RadioButton) v;

                if (rb == radioButtons[0]) {
                    // Asignar valores para Principiante
                    filas = 8;
                    columnas = 8;
                    contador = 0;
                    numeroMinas = 10;
                } else if (rb == radioButtons[1]) {
                    // Asignar valores para Amateur
                    filas = 12;
                    columnas = 12;
                    contador= 1;
                    numeroMinas = 30;
                } else if (rb == radioButtons[2]) {
                    // Asignar valores para Avanzado
                    filas = 16;
                    columnas = 16;
                    contador=2;
                    numeroMinas = 60;
                }
                // Inicializo el tablero según el nivel de dificultad seleccionado
                inicializarTablero(filas, columnas, numeroMinas);
            }
        };//fin onClickListener

        //Recorro el array de radioButtons para dejar siempre uno marcado
        //Y añado el evento a los botones
        for(int i=0; i<radioButtons.length; i++){
            radioButtons[contador].setChecked(true);
            radioButtons[i].setOnClickListener(listener);
        }

        //Funcionalidad del boton volver
        Button btnVolver = dialog.findViewById(R.id.btnVolver);
        btnVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Se cierra el diálogo cuando se hace click en volver
                dialog.dismiss();
            }
        });

        //Muestro el diálogo
        dialog.show();
    }

}