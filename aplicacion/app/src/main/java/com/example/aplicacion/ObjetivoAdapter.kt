package com.example.aplicacion

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.TextView
import com.example.aplicacion.model.Objetivo
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Adaptador personalizado para listar los objetivos del usuario.
 * Permite marcar objetivos como completados e indica visualmente aquellos que han vencido.
 *
 * @param context Contexto de la aplicación.
 * @param objetivos Lista de objetos Objetivo a mostrar.
 * @param onCompletadoChange Callback que notifica al Activity cuando se cambia el estado de completado.
 */
class ObjetivoAdapter(
    context: Context,
    private var objetivos: List<Objetivo>,
    private val onCompletadoChange: (Objetivo, Boolean) -> Unit
) : ArrayAdapter<Objetivo>(context, 0, objetivos) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_objetivo, parent, false)
        val objetivo = getItem(position) ?: return view

        val tvTexto = view.findViewById<TextView>(R.id.tvTextoObjetivo)
        val cbCompletado = view.findViewById<CheckBox>(R.id.cbCompletado)

        tvTexto.text = "${objetivo.tipo}: ${objetivo.valorObjetivo}\nLímite: ${objetivo.fechaLimite}"

        // Desactivamos el listener antes de establecer el estado para evitar disparos accidentales al reciclar vistas
        cbCompletado.setOnCheckedChangeListener(null)
        cbCompletado.isChecked = objetivo.completado

        // Lógica visual: resalta en rojo si la fecha límite pasó y el objetivo no se ha completado
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        try {
            val fechaLimite = sdf.parse(objetivo.fechaLimite)
            val hoy = Date()

            if (fechaLimite != null && fechaLimite.before(hoy) && !objetivo.completado) {
                tvTexto.setTextColor(Color.RED)
            } else {
                tvTexto.setTextColor(Color.parseColor("#333333"))
            }
        } catch (e: Exception) {
            tvTexto.setTextColor(Color.parseColor("#333333"))
        }

        // Listener para notificar el cambio de estado de completado
        cbCompletado.setOnCheckedChangeListener { _, isChecked ->
            onCompletadoChange(objetivo, isChecked)
        }

        return view
    }
}