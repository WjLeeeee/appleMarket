package com.example.applemarket


import android.app.Activity
import  android.app.NotificationChannel
import android.app.NotificationManager
import android.content.DialogInterface
import android.content.Intent
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.animation.AlphaAnimation
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        // MyAdapter를 초기화하고 결과 런처를 설정
        val adapter = MyAdapter(Product.dataList)
        resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data: Product? = result.data?.getParcelableExtra(Constants.UPDATE_DATA)
                data?.let {
                    val index = adapter.mItems.indexOfFirst { product -> product.productName == it.productName }
                    if (index != -1) {
                        adapter.mItems[index] = it
                        adapter.notifyItemChanged(index)
                    }
                }
            }
        }
        adapter.setResult(resultLauncher)


        with(binding){
            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(this@MainActivity)
            recyclerView.addItemDecoration(DividerItemDecoration(applicationContext, 1))

            notification.setOnClickListener { notification() }
        }

        //플로팅버튼이 서서이 보이게, 사라지게하는 애니메이션
        val fadeIn = AlphaAnimation(0f,1f).apply { duration=1000 }
        val fadeOut = AlphaAnimation(1f,0f).apply { duration=1000 }
        var isTop = true

        with(binding){
            recyclerView.addOnScrollListener(object: RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (!binding.recyclerView.canScrollVertically(-1)
                        && newState == RecyclerView.SCROLL_STATE_IDLE) {
                        binding.floatingBtn.startAnimation(fadeOut)
                        binding.floatingBtn.visibility = View.GONE
                        isTop = true
                    } else {
                        if(isTop) {
                            binding.floatingBtn.visibility = View.VISIBLE
                            binding.floatingBtn.startAnimation(fadeIn)
                            isTop = false
                        }
                    }
                }
            })
            floatingBtn.setOnClickListener {
                binding.recyclerView.smoothScrollToPosition(0)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showExitDialog()
            return true // 이벤트를 소비하여 기본 동작을 막음
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showExitDialog() {
        var builder = AlertDialog.Builder(this)
        builder.setTitle("종료")
        builder.setMessage("정말 종료하시겠습니까?")
        builder.setIcon(R.drawable.chat)
        builder.setCancelable(false)

        // 버튼 클릭시에 무슨 작업을 할 것인가!
        val listener = object : DialogInterface.OnClickListener {
            override fun onClick(p0: DialogInterface?, p1: Int) {
                when (p1) {
                    DialogInterface.BUTTON_POSITIVE -> {
                        finishAffinity()
                    }

                    DialogInterface.BUTTON_NEGATIVE -> {
                        p0?.dismiss()
                    }
                }
            }
        }

        builder.setPositiveButton("확인", listener)
        builder.setNegativeButton("취소", listener)

        builder.show()
    }

    fun notification() {
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        val builder: NotificationCompat.Builder
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 26 버전 이상
            val channelId = "one-channel"
            val channelName = "My Channel One"
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                // 채널에 다양한 정보 설정
                description = "My Channel One Description"
                setShowBadge(true)
                val uri: Uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                //알림음 소리. 다른소리를 원하면 다른 mp3파일을 넣어서 실행시키면 그 소리가 알림소리로 남.
                val audioAttributes = AudioAttributes.Builder()
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build()
                setSound(uri, audioAttributes)
                enableVibration(true)
            }
            // 채널을 NotificationManager에 등록
            manager.createNotificationChannel(channel)

            // 채널을 이용하여 builder 생성
            builder = NotificationCompat.Builder(this, channelId)

        } else {
            // 26 버전 이하, 사용권장X
            builder = NotificationCompat.Builder(this)
        }

        // 알림의 기본 정보
        builder.run {
            setSmallIcon(R.mipmap.ic_launcher)
            setWhen(System.currentTimeMillis())
            setContentTitle("키워드 알림")
            setContentText("설정한 키워드에 대한 알림이 도착했습니다!!")
        }

        manager.notify(11, builder.build())
    }
}