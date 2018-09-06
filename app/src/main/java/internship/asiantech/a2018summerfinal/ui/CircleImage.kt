package internship.asiantech.a2018summerfinal.ui

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Handler
import android.support.annotation.DrawableRes
import android.util.AttributeSet
import android.widget.ImageView
import internship.asiantech.a2018summerfinal.R
import java.lang.ref.WeakReference
import kotlin.properties.Delegates

class CircleImage : ImageView {
    private val rect: RectF = RectF()
    private val bitmapPaint: Paint = Paint()

    init {
        bitmapPaint.isAntiAlias = true
    }

    private var bitmap: Bitmap? = null
    private var bitmapWidth: Int = 0
    private var bitmapHeight: Int = 0
    private var radius: Float = 0f


    private var angle = 0
    private var angleStep = 2
    private var rotateMaker: RotateMaker? = null
    var isRunning by Delegates.observable(false) { _, _, newState: Boolean ->
        if (!newState) {
            rotateMaker?.endRotate()
        } else {
            rotateMaker = RotateMaker(this)
            rotateMaker?.start()
        }
    }
    private val customHandler: Handler = Handler()

    private var isReady: Boolean = false
    private var isSetupPending: Boolean = false

    constructor(context: Context) : super(context) {
        init()
    }

    @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyle: Int = 0) : super(context, attrs, defStyle) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.CircleImage, defStyle, 0)
        isRunning = a.getBoolean(R.styleable.CircleImage_isRunning, isRunning)
        angleStep = a.getInteger(R.styleable.CircleImage_angleStep, angleStep)
        if (isRunning) {
            rotateMaker = RotateMaker(this)
            rotateMaker?.start()
        }
        a.recycle()
        init()
    }

    private fun init() {
        isReady = true

        if (isSetupPending) {
            setup()
            isSetupPending = false
        }
    }

    override fun setImageResource(@DrawableRes resId: Int) {
        super.setImageResource(resId)
        initBitmap()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        super.setImageBitmap(bm)
        initBitmap()
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        initBitmap()
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        initBitmap()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setup()
    }

    override fun setPadding(left: Int, top: Int, right: Int, bottom: Int) {
        super.setPadding(left, top, right, bottom)
        setup()
    }

    override fun setPaddingRelative(start: Int, top: Int, end: Int, bottom: Int) {
        super.setPaddingRelative(start, top, end, bottom)
        setup()
    }

    override fun onDraw(canvas: Canvas) {
        if (bitmap == null) {
            return
        }
        canvas.save()
        canvas.rotate(angle.toFloat(), rect.centerX(), rect.centerY())
        canvas.drawCircle(rect.centerX(), rect.centerY(), radius, bitmapPaint)
        canvas.restore()
    }

    private fun initBitmap() {
        bitmap = getBitmapFromDrawable(drawable)
        setup()
    }

    private fun getBitmapFromDrawable(drawable: Drawable): Bitmap? {
        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }
        return if (drawable is ColorDrawable) {
            Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
    }

    private fun setup() {
        if (!isReady) {
            isSetupPending = true
            return
        }

        if (width == 0 || height == 0) {
            return
        }

        if (bitmap == null) {
            invalidate()
            return
        }

        bitmap?.let {
            bitmapPaint.shader = BitmapShader(it, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
            bitmapHeight = it.height
            bitmapWidth = it.width
        }
        rect.set(calculateBound())
        radius = Math.min(rect.width() / 2.0f, rect.height() / 2.0f)
        bitmapPaint.shader?.setLocalMatrix(getShaderMatrix())
        invalidate()
    }

    private fun calculateBound(): RectF {
        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom

        val len = Math.min(contentWidth, contentHeight)
        val left = (contentWidth - len) / 2f + paddingLeft
        val top = (contentHeight - len) / 2f + paddingTop

        return RectF(left, top, left + len, top + len)
    }

    private fun getShaderMatrix(): Matrix {
        val scale: Float
        var dx = 0f
        var dy = 0f
        val matrix = Matrix()

        if (bitmapWidth * rect.height() > bitmapHeight * rect.width()) {
            scale = rect.height() / bitmapHeight.toFloat()
            dx = (rect.width() - bitmapWidth * scale) / 2f
        } else {
            scale = rect.width() / bitmapWidth.toFloat()
            dy = (rect.height() - bitmapHeight * scale) / 2f
        }
        matrix.setScale(scale, scale)
        matrix.postTranslate(dx + rect.left, dy + rect.top)

        return matrix
    }

    companion object {
        const val COLOR_DRAWABLE_DIMENSION = 2
    }

    class RotateMaker() : Thread() {
        private lateinit var circleImageReference: WeakReference<CircleImage>
        private var isRun = true

        constructor(circleImage: CircleImage) : this() {
            circleImageReference = WeakReference(circleImage)
        }

        override fun run() {
            while (isRun) {
                circleImageReference.get()?.let {
                    it.angle += it.angleStep
                    if (it.angle > 360) {
                        it.angle -= 360
                    }
                    it.customHandler.post { it.invalidate() }
                }
                Thread.sleep(50)
            }
        }

        fun endRotate() {
            isRun = false
        }
    }
}
