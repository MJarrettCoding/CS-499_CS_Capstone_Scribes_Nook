package com.example.scribesnook.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.scribesnook.LoginActivity
import com.example.scribesnook.Prefs
import com.example.scribesnook.R

class MenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_menu, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val logoutButton = view.findViewById<Button>(R.id.btn_logout)

        logoutButton.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        // 🔹 Clear saved JWT
        Prefs.clear(requireContext())

        // 🔹 Go to login screen
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }
}
