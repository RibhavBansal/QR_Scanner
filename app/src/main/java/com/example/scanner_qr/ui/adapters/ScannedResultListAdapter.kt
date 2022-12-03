package com.example.scanner_qr.ui.adapters

import android.app.SearchManager.OnCancelListener
import android.content.Context
import android.content.DialogInterface
import android.content.DialogInterface.OnClickListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.scanner_qr.R
import com.example.scanner_qr.db.DBHelperI
import com.example.scanner_qr.db.entities.QrResult
import com.example.scanner_qr.ui.dialogs.QrCodeResultDialog
import com.example.scanner_qr.ui.utils.gone
import com.example.scanner_qr.ui.utils.toFormattedDisplay
import com.example.scanner_qr.ui.utils.visible
import kotlinx.android.synthetic.main.layout_single_item_qr_result.view.*
import java.text.FieldPosition

class ScannedResultListAdapter (
    var dbHelperI: DBHelperI,
    var context: Context,
    var listOfScannedResults: MutableList<QrResult>
        ) : RecyclerView.Adapter<ScannedResultListAdapter.ScannedResultListViewHolder>(){

    private var resultDialog = QrCodeResultDialog(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScannedResultListViewHolder {
        return ScannedResultListViewHolder(LayoutInflater.from(context).inflate(R.layout.layout_single_item_qr_result,parent,false))
    }

    override fun onBindViewHolder(holder: ScannedResultListViewHolder, position: Int) {
        holder.bind(listOfScannedResults[position],position)
    }

    override fun getItemCount(): Int {
        return  listOfScannedResults.size
    }

    inner class ScannedResultListViewHolder(var view: View):RecyclerView.ViewHolder(view){
        fun bind(qrResult: QrResult,position: Int){
            view.result.text = qrResult.result
            view.tvTime.text = qrResult.calendar.toFormattedDisplay()
            setFavourite(qrResult.favourite)
            onClicks(qrResult,position)
        }

        private fun onClicks(qrResult: QrResult,position: Int) {
            view.setOnClickListener{
                resultDialog.show(qrResult)
            }

            view.setOnLongClickListener{
                showDeleteDialog(qrResult,position)
                return@setOnLongClickListener true
            }
        }

        private fun showDeleteDialog(qrResult: QrResult, position: Int) {
            AlertDialog.Builder(context,R.style.CustomAlertDialog)
                .setTitle("Delete")
                .setMessage("Are you sure to delete this record?")
                .setPositiveButton("Delete") { p0, p1 ->
                    deleteThisRecord(qrResult,position)
                }
                .setNegativeButton("Cancel") { p0, p1 ->
                    p0.cancel()
                }.show()
        }

        private fun deleteThisRecord(qrResult: QrResult, position: Int) {
            dbHelperI.deleteQrResult(qrResult.id!!)
            listOfScannedResults.removeAt(position)
            notifyItemRemoved(position)
        }

        private fun setFavourite(favourite: Boolean) {
            if(favourite) {
                view.favouriteIcon.visible()
            }else{
                view.favouriteIcon.gone()
            }
        }
    }


}