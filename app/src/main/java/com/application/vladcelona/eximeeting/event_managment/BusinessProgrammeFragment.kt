package com.application.vladcelona.eximeeting.event_managment

import android.os.Bundle
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.application.vladcelona.eximeeting.R
import com.application.vladcelona.eximeeting.databinding.FragmentBusinessProgrammeBinding
import com.google.gson.internal.LinkedTreeMap

private const val TAG = "BusinessProgrammeFragment"

class BusinessProgrammeFragment : Fragment() {

    private lateinit var binding: FragmentBusinessProgrammeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBusinessProgrammeBinding.inflate(inflater, container, false)

        val receivedBusinessProgramme = arguments
            ?.getSerializable("businessProgramme") as LinkedHashMap<String, ArrayList<String?>?>?

        val convertedBusinessProgramme: LinkedTreeMap<String,
                ArrayList<String?>?>? = LinkedTreeMap()

        receivedBusinessProgramme?.forEach {
                (key, value) -> convertedBusinessProgramme?.set(key, value) }

        Log.i(TAG, receivedBusinessProgramme.toString())

        val businessProgrammeText: StringBuilder = StringBuilder()
        if (receivedBusinessProgramme != null) {
            for ((key, value) in receivedBusinessProgramme) {
                businessProgrammeText.append("<b>${key}</b>\n")
                businessProgrammeText.append("<p>${value}\n\n</p>")
            }
        }

        binding.mainBusinessProgrammeText.text = Html.fromHtml(businessProgrammeText.toString())

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(): BusinessProgrammeFragment {
            return BusinessProgrammeFragment()
        }
    }
}