package com.example.scanner_qr.ui.scanned_history

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.scanner_qr.R
import com.example.scanner_qr.db.DBHelper
import com.example.scanner_qr.db.DBHelperI
import com.example.scanner_qr.db.database.QrResultDatabase
import com.example.scanner_qr.db.entities.QrResult
import com.example.scanner_qr.ui.adapters.ScannedResultListAdapter
import com.example.scanner_qr.ui.utils.gone
import com.example.scanner_qr.ui.utils.visible
import kotlinx.android.synthetic.main.fragment_scanned_history.view.*
import kotlinx.android.synthetic.main.layout_header_history.view.*

class ScannedHistoryFragment : Fragment() {

    enum class ResultListType{
        ALL_RESULT,FAVOURITE_RESULT
    }

    companion object {

        private const val ARGUMENT_RESULT_LIST_TYPE = "ArgumentResultListType"

        fun newInstance(screenType: ResultListType): ScannedHistoryFragment {
            val bundle =Bundle()
            bundle.putSerializable(ARGUMENT_RESULT_LIST_TYPE,screenType)
            val fragment = ScannedHistoryFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private lateinit var resultType:ResultListType

    private lateinit var mView: View

    private lateinit var dbHelperI: DBHelperI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        handleArgument()
    }

    private fun handleArgument() {
        resultType = arguments?.getSerializable(ARGUMENT_RESULT_LIST_TYPE) as ResultListType
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_scanned_history, container, false)
        init()
        showListOfResults()
        setSwipeRefreshLayout()
        onClicks()
        return mView
    }

    private fun onClicks() {
        mView.removeAll.setOnClickListener{
            showRemoveAllScannedResultDialog()
        }
    }

    private fun showRemoveAllScannedResultDialog() {
        AlertDialog.Builder(requireContext(),R.style.CustomAlertDialog)
            .setTitle("Delete All")
            .setMessage("Are you sure to delete all records?")
            .setPositiveButton("Delete") { p0, p1 ->
                clearAllRecords()
            }
            .setNegativeButton("Cancel") { p0, p1 ->
                p0.cancel()
            }.show()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun clearAllRecords() {
        when(resultType){
            ResultListType.ALL_RESULT -> dbHelperI.deleteAllQrScannerResults()
            ResultListType.FAVOURITE_RESULT -> dbHelperI.deleteAllFavouriteQrScannerResults()
        }
        mView.scannedHistoryRecyclerView.adapter?.notifyDataSetChanged()
        showAllResults()
    }

    private fun setSwipeRefreshLayout() {
        mView.swipeRefresh.setOnRefreshListener {
            mView.swipeRefresh.isRefreshing = false
            showListOfResults()
        }
    }

    private fun showListOfResults() {
        when(resultType){
            ResultListType.ALL_RESULT -> {
                showAllResults()
            }
            ResultListType.FAVOURITE_RESULT -> {
                showFavouriteResults()
            }
        }
    }

    private fun showAllResults() {
        val listOfAllResult = dbHelperI.getAllQrScannedResult()
        showResults(listOfAllResult)
        mView.tvHeaderText.text = "Recent Scanned Results"
    }

    private fun showResults(lisOfQrResults: List<QrResult>) {
        if(lisOfQrResults.isEmpty()){
            showEmptyState()
        }else{
            initRecyclerView(lisOfQrResults)
        }
    }

    private fun initRecyclerView(lisOfQrResults: List<QrResult>) {
        mView.scannedHistoryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        mView.scannedHistoryRecyclerView.adapter = ScannedResultListAdapter(dbHelperI,requireContext(),lisOfQrResults.toMutableList())
        showRecyclerView()
    }

    private fun showRecyclerView() {
        mView.scannedHistoryRecyclerView.visible()
        mView.noResultFound.gone()
        mView.removeAll.visible()
    }

    private fun showEmptyState() {
        mView.scannedHistoryRecyclerView.gone()
        mView.noResultFound.visible()
        mView.removeAll.gone()
    }

    private fun showFavouriteResults() {
        val listOfFavouriteResult = dbHelperI.getAllFavouriteQrScannedResult()
        showResults(listOfFavouriteResult)
        mView.tvHeaderText.text = "Recent Favourite Results"
    }

    private fun init() {
        dbHelperI = DBHelper(QrResultDatabase.getAppDatabase(requireContext())!!)
    }
}