package com.uth.apputh;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.uth.apputh.dao.ClienteDAO;
import com.uth.apputh.models.Cliente;

public class MainActivity extends AppCompatActivity {

    TextInputEditText nombre, apellido, edad, correo;
    MaterialButton btnAgregar, btnVerClientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        nombre = findViewById(R.id.nombre);
        apellido = findViewById(R.id.apellido);
        edad = findViewById(R.id.edad);
        correo = findViewById(R.id.correo);

        btnAgregar = findViewById(R.id.btnAgregar);
        btnVerClientes = findViewById(R.id.btnVerClientes);

        btnAgregar.setOnClickListener(v -> {
            // Limpiar errores previos
            nombre.setError(null);
            apellido.setError(null);
            edad.setError(null);
            correo.setError(null);

            // 1. Leer los valores del formulario y eliminar espacios al inicio y al final
            String nombreTxt = nombre.getText() != null ? nombre.getText().toString().trim() : "";
            String apellidoTxt = apellido.getText() != null ? apellido.getText().toString().trim() : "";
            String edadTxt = edad.getText() != null ? edad.getText().toString().trim() : "";
            String correoTxt = correo.getText() != null ? correo.getText().toString().trim() : "";

            // 2. Comprobar que el nombre esté completo (obligatorio, no solo espacios)
            if (nombreTxt.isEmpty()) {
                nombre.setError("El nombre es obligatorio");
                nombre.requestFocus();
                return;
            }

            // 3. Comprobar que el apellido esté completo (obligatorio, no solo espacios)
            if (apellidoTxt.isEmpty()) {
                apellido.setError("El apellido es obligatorio");
                apellido.requestFocus();
                return;
            }

            // 4. Comprobar y validar la edad (obligatoria, número entero mayor que cero)
            if (edadTxt.isEmpty()) {
                edad.setError("La edad es obligatoria");
                edad.requestFocus();
                return;
            }

            int edadNum;
            try {
                edadNum = Integer.parseInt(edadTxt);
                if (edadNum <= 0) {
                    edad.setError("La edad debe ser mayor que cero");
                    edad.requestFocus();
                    return;
                }
            } catch (NumberFormatException e) {
                edad.setError("Ingrese una edad numérica válida");
                edad.requestFocus();
                return;
            }

            // 5. Comprobar y validar el formato del correo (obligatorio, formato de correo válido)
            if (correoTxt.isEmpty()) {
                correo.setError("El correo es obligatorio");
                correo.requestFocus();
                return;
            }

            if (!Patterns.EMAIL_ADDRESS.matcher(correoTxt).matches()) {
                correo.setError("Ingrese un formato de correo válido");
                correo.requestFocus();
                return;
            }

            // 6. Crear un objeto Cliente y asignar valores mediante métodos set
            Cliente cliente = new Cliente();
            cliente.setNombre(nombreTxt);
            cliente.setApellido(apellidoTxt);
            cliente.setEdad(edadNum);
            cliente.setCorreo(correoTxt);

            // 7. Utilizar ClienteDAO.insertar(cliente) para guardar el registro
            ClienteDAO dao = new ClienteDAO(this);
            long resultado = dao.insertar(cliente);

            // 8. Revisar el resultado de la inserción
            if (resultado > 0) {
                // Mostrar "Persona registrada correctamente" cuando el guardado sea exitoso
                Toast.makeText(
                        this,
                        "Persona registrada correctamente",
                        Toast.LENGTH_SHORT
                ).show();

                // Limpiar los campos únicamente después de guardar correctamente
                nombre.setText("");
                apellido.setText("");
                edad.setText("");
                correo.setText("");

                nombre.requestFocus();
            } else {
                // Si ocurre un error, informar al usuario y conservar los datos del formulario
                Toast.makeText(
                        this,
                        "Error al registrar la persona",
                        Toast.LENGTH_SHORT
                ).show();
            }
        });

        btnVerClientes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ClientesActivity.class);
            startActivity(intent);
        });
    }
}