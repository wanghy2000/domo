package com.sywang

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Parcelable
import android.provider.MediaStore
import android.provider.MediaStore.MediaColumns
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sywang.adapter.RVListAdapter
import com.sywang.model.MediaFileData
import java.io.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity(){

    var nList: RecyclerView? = null
    var nAdapter: RVListAdapter? = null

    enum class MediaStoreFileType(
        val externalContentUri: Uri,
        val mimeType: String,
        val pathByDCIM: String
    ) {
        IMAGE(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*", "/image"),
        AUDIO(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, "audio/*", "/audio"),
        VIDEO(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, "video/*", "/video");
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        nList = findViewById(R.id.sy_list)

        nAdapter = RVListAdapter(object: RVListAdapter.Callback {
            override fun onClick(data: MediaFileData) {
                logd("list item click")

                val intent = Intent(this@MainActivity, ViewActivity::class.java)
                intent.putExtra("sy", data.uri.toString())
                startActivity(intent)
            }
        })

        val manager = LinearLayoutManager(this)
        manager.orientation = LinearLayoutManager.VERTICAL
        nList?.layoutManager = manager
        nList?.adapter = nAdapter

        /*nList?.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    if (!recyclerView.canScrollVertically(-1)) { //top

                    } else if (!recyclerView.canScrollVertically(1)) { //bottom
                        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                            //loadData()
                        }
                    } else { //idle

                    }
                }
            }
        )*/

        getFileList(this, MediaStoreFileType.VIDEO)

    }

    fun getFileList(context: Context, type: MediaStoreFileType): List<MediaFileData> {
        val fileList = mutableListOf<MediaFileData>()
        val projection = arrayOf(
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.DISPLAY_NAME,
            MediaStore.Files.FileColumns.DATE_TAKEN
        )

        val sortOrder = "${MediaStore.Files.FileColumns.DATE_TAKEN} DESC"

        val cursor = context.contentResolver.query(
            type.externalContentUri,
            projection,
            null,
            null,
            sortOrder
        )

        cursor?.use {
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID)
            val dateTakenColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_TAKEN)
            val displayNameColumn =
                cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val dateTaken = Date(cursor.getLong(dateTakenColumn))
                val displayName = cursor.getString(displayNameColumn)
                val contentUri = Uri.withAppendedPath(
                    type.externalContentUri,
                    id.toString()
                )

            //    logd("id: $id, display_name: $displayName, date_taken: $dateTaken, content_uri: $contentUri\n")
                fileList.add(MediaFileData(id, dateTaken, displayName, contentUri))

              //  logd("앙 승윤띠!!" + fileList)
            }
        }

        logd("앙 승윤띠!! 투투투투 ::: " + fileList)

        nAdapter?.setFileList(fileList)
        //nAdapter?.addData(fileList)
        /*for (i in 0 until fileList.size) {

            logd("tmddbs durls1 ${fileList[i]}")

        }*/

        return fileList
    }




    fun logd(logtext: String) {
        Log.d("승윤", "wang $logtext")
    }
}