package com.lahielera.appcontactos.ui.menu

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.lahielera.appcontactos.R
import com.lahielera.appcontactos.databinding.FragmentFirstBinding
import com.lahielera.appcontactos.model.Contacto

class ContactosFragment : Fragment(), ContactosAdapter.OnContactoCLickListener {

    private lateinit var binding: FragmentFirstBinding
    private lateinit var rvContactos: RecyclerView
    private lateinit var adapter: ContactosAdapter
    private lateinit var viewModel: ContactosViewModel

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_first,
            container,
            false
        )
        loadRecyclerView()
        binding.setLifecycleOwner(this)
        viewModel = ViewModelProvider(this).get(ContactosViewModel::class.java)
        getContactos()
        viewModel.onSucess.observe(viewLifecycleOwner, Observer { onSuccess ->
            if (onSuccess)
                Toast.makeText(requireContext(), "Registro borrado", Toast.LENGTH_SHORT).show()
        })
        return binding.root
    }

    private fun getContactos() {
        viewModel.contactos.observe(viewLifecycleOwner, Observer { contactos ->
            adapter.addData(contactos, this)
        })
    }

    private fun loadRecyclerView() {
        rvContactos = binding.rvContactos
        rvContactos.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        adapter = ContactosAdapter()
        rvContactos.adapter = adapter
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onPause() {
        super.onPause()
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.hide()
        viewModel.resetOnSuccessEvent()
    }

    override fun onResume() {
        super.onResume()
        activity?.findViewById<FloatingActionButton>(R.id.fab)?.show()
        viewModel.updateContactos()
        Log.d("Contactos", "OnResume")
    }

    override fun borrarContacto(uid: String) {
        viewModel.deleteContacto(uid)
    }

    override fun verContacto(contacto: Contacto) {
        findNavController().navigate(ContactosFragmentDirections.actionFirstFragmentToSecondFragment(contacto))
    }


}