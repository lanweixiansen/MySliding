package com.example.mysliding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.mysliding.databinding.HomeFragmentDemoSlidingBinding
import com.example.spk.sliding.SlidingUtils

class SlidingDemoFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return HomeFragmentDemoSlidingBinding.inflate(layoutInflater).root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SlidingUtils.showSliding(this, SlidingTestView(requireContext(), "Fragment"))
    }
}