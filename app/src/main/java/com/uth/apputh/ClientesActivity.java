package com.uth.apputh;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.uth.apputh.dao.ClienteDAO;
import com.uth.apputh.models.Cliente;

import java.util.ArrayList;

public class ClientesActivity extends AppCompatActivity {

    ListView listaClientes;
    ClienteDAO clienteDAO;
    ArrayList<Cliente> clientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_clientes);

        listaClientes = findViewById(R.id.listaClientes);
        listaClientes.setEmptyView(findViewById(R.id.layoutVacio));

        MaterialButton btnRegresar = findViewById(R.id.btnRegresar);
        if (btnRegresar != null) {
            btnRegresar.setOnClickListener(v -> finish());
        }

        clienteDAO = new ClienteDAO(this);

        cargarClientes();

        listaClientes.setOnItemLongClickListener((parent, view, position, id) -> {
            Cliente cliente = clientes.get(position);

            new AlertDialog.Builder(this)
                    .setTitle("Eliminar cliente")
                    .setMessage("¿Desea eliminar a " + cliente.getNombre() + " " + cliente.getApellido() + "?")
                    .setPositiveButton("Sí", (dialog, which) -> {
                        clienteDAO.eliminar(cliente.getId());
                        cargarClientes();
                        Toast.makeText(
                                this,
                                "Cliente eliminado",
                                Toast.LENGTH_SHORT
                        ).show();
                    })
                    .setNegativeButton("No", null)
                    .show();

            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarClientes();
    }

    private void cargarClientes() {
        clientes = clienteDAO.listar();

        ArrayAdapter<Cliente> adapter = new ArrayAdapter<Cliente>(
                this,
                R.layout.item_cliente,
                clientes
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                if (convertView == null) {
                    convertView = getLayoutInflater().inflate(R.layout.item_cliente, parent, false);
                }

                Cliente cliente = getItem(position);
                if (cliente != null) {
                    TextView tvAvatar = convertView.findViewById(R.id.tvAvatarLetra);
                    TextView tvNombre = convertView.findViewById(R.id.tvNombreCompleto);
                    TextView tvCorreo = convertView.findViewById(R.id.tvCorreo);
                    TextView tvEdad = convertView.findViewById(R.id.tvEdad);

                    String nombre = cliente.getNombre() != null ? cliente.getNombre().trim() : "";
                    String inicial = !nombre.isEmpty() ? nombre.substring(0, 1).toUpperCase() : "?";

                    tvAvatar.setText(inicial);
                    tvNombre.setText(cliente.getNombre() + " " + cliente.getApellido());
                    tvCorreo.setText(cliente.getCorreo());
                    tvEdad.setText(cliente.getEdad() + " años");
                }

                return convertView;
            }
        };

        listaClientes.setAdapter(adapter);
    }
}