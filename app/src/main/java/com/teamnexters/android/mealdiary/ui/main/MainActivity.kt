package com.teamnexters.android.mealdiary.ui.main

import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.teamnexters.android.mealdiary.R
import com.teamnexters.android.mealdiary.base.BaseActivity
import com.teamnexters.android.mealdiary.data.model.ListItem
import com.teamnexters.android.mealdiary.databinding.ActivityMainBinding
import com.teamnexters.android.mealdiary.util.Navigator
import com.teamnexters.android.mealdiary.util.extension.observe
import com.teamnexters.android.mealdiary.util.extension.subscribeOf
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

internal class MainActivity : BaseActivity<ActivityMainBinding, MainViewModel.ViewModel>() {

    private val diaryAdapter: DiaryAdapter by inject()

    override val layoutResId: Int = R.layout.activity_main

    override val viewModel: MainViewModel.ViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.viewModel = viewModel

        initializeRecyclerView()

        disposables.addAll(
                viewModel.outputs.ofNavigateToWrite()
                        .observeOn(schedulerProvider.ui())
                        .subscribeOf(onNext = { Navigator.navigateToWrite(this, it) }),

                viewModel.outputs.ofShowDiaryDialog()
                        .observeOn(schedulerProvider.ui())
                        .subscribeOf(onNext = {
                            AlertDialog.Builder(this)
                                    .setMessage(it.content)
                                    .setNegativeButton("수정") { _, _ -> viewModel.inputs.toClickModify(it.id) }
                                    .setPositiveButton("삭제") { _, _ -> viewModel.inputs.toClickDelete(it.id) }
                                    .show()
                        })
        )

        observe(viewModel.diaryItems) { diaryAdapter.submitList(it) }
    }

    private fun initializeRecyclerView() {
        binding.rvDiary.run {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = diaryAdapter
        }

        diaryAdapter.setCallbacks(object : DiaryAdapter.Callbacks {
            override fun onClickDiary(item: ListItem.DiaryItem) {
                viewModel.toClickDiaryItem(item)
            }
        })
    }
}
