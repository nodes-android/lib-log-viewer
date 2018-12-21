package com.nodesagency.logviewer.screens.categories

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.nodesagency.logviewer.Logger
import com.nodesagency.logviewer.R
import com.nodesagency.logviewer.data.model.Category

internal class CategoriesFragment : Fragment(), OnCategorySelectListener {

    private lateinit var categoriesRecyclerView: RecyclerView
    private lateinit var categoriesAdapter: CategoriesAdapter
    private lateinit var layoutManager: RecyclerView.LayoutManager
    private lateinit var categoriesViewModel: CategoriesViewModel
    private lateinit var oCategoryFragmentListener: CategoriesFragmentListener

    companion object {
        fun newInstance() = CategoriesFragment().apply {
            arguments = Bundle()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        oCategoryFragmentListener = context as? CategoriesFragmentListener
                ?: throw ClassCastException("Context must implement OnCategoriesFragmentListener.")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
        categoriesViewModel = ViewModelProviders
            .of(this, CategoriesViewModelFactory(Logger.getRepository()))
            .get(CategoriesViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_categories, container, false)

        categoriesRecyclerView = view.findViewById(R.id.categoriesView)
        val dividerDecoration = DividerItemDecoration(inflater.context, VERTICAL)

        layoutManager = LinearLayoutManager(context)
        categoriesAdapter = CategoriesAdapter()

        categoriesRecyclerView.layoutManager = layoutManager
        categoriesRecyclerView.adapter = categoriesAdapter
        categoriesRecyclerView.addItemDecoration(dividerDecoration)

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater?.inflate(R.menu.menu_categories, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        categoriesViewModel
            .categoryList
            .observe(this, Observer(categoriesAdapter::submitList))
    }


    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when(item?.itemId) {
            R.id.actionClearLogs -> {
                showDeleteLogsConfirmation()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showDeleteLogsConfirmation() {
        AlertDialog.Builder(context)
            .setTitle(R.string.clear_logs_dialog_title)
            .setMessage(R.string.clear_logs_dialog_message)
            .setNegativeButton(android.R.string.cancel) { dialogInterface, _ -> dialogInterface.dismiss() }
            .setPositiveButton(android.R.string.yes) {_, _ -> categoriesViewModel.clearAllLogs()}
            .show()
    }

    override fun onResume() {
        super.onResume()
        categoriesAdapter.onCategorySelectListener = this
    }

    override fun onCategorySelected(category: Category) {
        oCategoryFragmentListener.onCategorySelected(category)
    }

    override fun onCategoryPinButtonPressed(category: Category) {
        categoriesViewModel.pinCategory(category)
    }

    override fun onPause() {
        super.onPause()

        categoriesAdapter.onCategorySelectListener = null
    }

    interface CategoriesFragmentListener {
        fun onCategorySelected(category: Category)
    }

}