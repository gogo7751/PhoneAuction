package com.eric.phoneauction

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.Shape
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import app.appworks.school.publisher.network.LoadApiStatus
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.eric.phoneauction.data.Event
import com.eric.phoneauction.util.TimeUtil
import com.eric.phoneauction.detailAuctionFragment.DetailCircleAdapter
import com.eric.phoneauction.detailAuctionFragment.DetailGalleryAdapter
import com.eric.phoneauction.homeFragment.HomeAdapter
import com.eric.phoneauction.util.Util

@BindingAdapter("events")
fun bindRecyclerView(recyclerView: RecyclerView, homeItems: List<Event>?) {
    homeItems?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is HomeAdapter -> submitList(it)
            }
        }
    }
}

@BindingAdapter("setupApiErrorMessage")
fun bindApiErrorMessage(view: TextView, message: String?) {
    when (message) {
        null, "" -> {
            view.visibility = View.GONE
        }
        else -> {
            view.text = message
            view.visibility = View.VISIBLE
        }
    }
}

@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = it.toUri().buildUpon().scheme("https").build()
        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.place_holder)
                    .error(R.drawable.place_holder))
            .into(imgView)
    }
}

@BindingAdapter("itemPosition", "itemCount")
fun setupPaddingForGridItems(layout: ConstraintLayout, position: Int, count: Int) {

    val outsideHorizontal = PhoneAuctionApplication.instance.resources.getDimensionPixelSize(R.dimen.space_outside_horizontal_catalog_item)
    val insideHorizontal = PhoneAuctionApplication.instance.resources.getDimensionPixelSize(R.dimen.space_inside_horizontal_catalog_item)
    val outsideVertical = PhoneAuctionApplication.instance.resources.getDimensionPixelSize(R.dimen.space_outside_vertical_catalog_item)
    val insideVertical = PhoneAuctionApplication.instance.resources.getDimensionPixelSize(R.dimen.space_inside_vertical_catalog_item)

    val layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.WRAP_CONTENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)

    when {
        position == 0 -> { // first item and confirm whether only 1 row
            layoutParams.setMargins(outsideHorizontal, outsideVertical, insideHorizontal, if (count > 2) insideVertical else outsideVertical)
        }
        position == 1 -> { // second item and confirm whether only 1 row
            layoutParams.setMargins(insideHorizontal, outsideVertical, outsideHorizontal, if (count > 2) insideVertical else outsideVertical)
        }
        count % 2 == 0 && position == count - 1 -> { // count more than 2 and item count is even
            layoutParams.setMargins(insideHorizontal, insideVertical, outsideHorizontal, outsideVertical)
        }
        (count % 2 == 1 && position == count - 1) || (count % 2 == 0 && position == count - 2) -> {
            layoutParams.setMargins(outsideHorizontal, insideVertical, insideHorizontal, outsideVertical)
        }
        position % 2 == 0 -> { // even
            when (position) {
                count - 1 -> layoutParams.setMargins(insideHorizontal, insideVertical, outsideHorizontal, outsideVertical) // last 1
                count - 2 -> layoutParams.setMargins(outsideHorizontal, insideVertical, insideHorizontal, outsideVertical) // last 2
                else -> layoutParams.setMargins(outsideHorizontal, insideVertical, insideHorizontal, insideVertical)
            }
        }
        position % 2 == 1 -> { // odd
            when (position) {
                count - 1 -> layoutParams.setMargins(outsideHorizontal, insideVertical, insideHorizontal, outsideVertical) // last 1
                else -> layoutParams.setMargins(insideHorizontal, insideVertical, outsideHorizontal, insideVertical)
            }
        }
    }

    layout.layoutParams = layoutParams
}

@BindingAdapter("setupApiStatus")
fun bindApiStatus(view: ProgressBar, status: LoadApiStatus?) {
    when (status) {
        LoadApiStatus.LOADING -> view.visibility = View.VISIBLE
        LoadApiStatus.DONE, LoadApiStatus.ERROR -> view.visibility = View.GONE
    }
}

/**
 * Displays currency price to [TextView] by [Int]
 */
@BindingAdapter("price")
fun bindPrice(textView: TextView, price: Int?) {
    price?.let { textView.text = PhoneAuctionApplication.instance.getString(R.string.nt_dollars_, it) }
}

@BindingAdapter("timeToDisplayFormat")
fun bindDisplayFormatTime(textView: TextView, time: Long?) {
    textView.text = time?.let { TimeUtil.StampToDate(it) }
}


@BindingAdapter("images")
fun bindRecyclerViewWithImages(recyclerView: RecyclerView, images: List<String>?) {
    images?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is DetailGalleryAdapter -> {
                    submitImages(it)
                }
            }
        }
    }
}


/**
 * Adds decoration to [RecyclerView]
 */
@BindingAdapter("addDecoration")
fun bindDecoration(recyclerView: RecyclerView, decoration: RecyclerView.ItemDecoration?) {
    decoration?.let { recyclerView.addItemDecoration(it) }
}

@BindingAdapter("count")
fun bindRecyclerViewByCount(recyclerView: RecyclerView, count: Int?) {
    count?.let {
        recyclerView.adapter?.apply {
            when (this) {
                is DetailCircleAdapter -> {
                    submitCount(it)
                }
            }
        }
    }
}

@BindingAdapter("circleStatus")
fun bindDetailCircleStatus(imageView: ImageView, isSelected: Boolean = false) {
    imageView.background = ShapeDrawable(object : Shape() {
        override fun draw(canvas: Canvas, paint: Paint) {

            paint.color = Util.getColor(R.color.white)
            paint.isAntiAlias = true

            when (isSelected) {
                true -> {
                    paint.style = Paint.Style.FILL
                }
                false -> {
                    paint.style = Paint.Style.STROKE
                    paint.strokeWidth = PhoneAuctionApplication.instance.resources
                        .getDimensionPixelSize(R.dimen.edge_detail_circle).toFloat()
                }
            }

            canvas.drawCircle(this.width / 2, this.height / 2,
                PhoneAuctionApplication.instance.resources
                    .getDimensionPixelSize(R.dimen.radius_detail_circle).toFloat(), paint)
        }
    })
}