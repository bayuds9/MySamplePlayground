package id.flowerencee.mysampleplayground.ui

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.util.Xml
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.text.isDigitsOnly
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.R.attr
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import id.flowerencee.mysampleplayground.R
import id.flowerencee.mysampleplayground.databinding.ItemContentBinding
import id.flowerencee.mysampleplayground.databinding.ItemFormBinding
import id.flowerencee.mysampleplayground.databinding.LayoutFormBinding
import id.flowerencee.mysampleplayground.objects.CardData
import id.flowerencee.mysampleplayground.objects.CheckBoxData
import id.flowerencee.mysampleplayground.objects.Constants
import id.flowerencee.mysampleplayground.objects.FormData
import id.flowerencee.mysampleplayground.objects.TextData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FormsView : ConstraintLayout {
    companion object {
        class CardAdapter(
            private val cardListener: (CardData) -> Unit
        ) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {
            private val data = ArrayList<CardData>()

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(item: CardData) = with(itemView) {
                    val bindData = ItemFormBinding.bind(this)
                    bindData.cardContainer.toShow()
                    bindData.textContainer.toShow()
                    item.title?.let {
                        bindData.tvTitle.apply {
                            text = it
                            setVisible(it.isNotEmpty())
                        }
                    }
                    item.description?.let {
                        bindData.apply {
                            tvDescription.text = it
                            setVisible(it.isNotEmpty())
                        }
                    }
                    when (item.position) {
                        Constants.CONTENT_POSITION.TOP -> {
                            bindData.imgTop.apply {
                                toShow()
                                item.imageUrl?.let {
                                    loadImage(context, it, this, R.drawable.ic_launcher_foreground)
                                }
                            }
                        }

                        Constants.CONTENT_POSITION.START -> {
                            bindData.imgStart.apply {
                                toShow()
                                item.imageUrl?.let {
                                    loadImage(context, it, this, R.drawable.ic_launcher_foreground)
                                }
                            }
                        }

                        Constants.CONTENT_POSITION.END -> {
                            bindData.imgEnd.apply {
                                toShow()
                                item.imageUrl?.let {
                                    loadImage(context, it, this, R.drawable.ic_launcher_foreground)
                                }
                            }
                        }

                        else -> {
                            // nothing to do
                        }
                    }
                    bindData.cardConstraintContainer.setOnClickListener {
                        cardListener(item)
                    }
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_form, parent, false)
                )
            }

            override fun getItemCount(): Int = data.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(data[position])
            }

            fun setData(list: ArrayList<CardData>) {
                data.clear()
                data.addAll(list)
                notifyDataSetChanged()
            }

        }

        class TextAdapter(
            private val textListener: (TextData) -> Unit
        ) : RecyclerView.Adapter<TextAdapter.ViewHolder>() {
            private val data = ArrayList<TextData>()

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(item: TextData) = with(itemView) {
                    val bindData = ItemFormBinding.bind(this)
                    bindData.tvText.apply {
                        item.text?.let {
                            setVisible(it.isNotEmpty())
                            text = it
                        }
                        item.clickable.let {
                            isClickable = it
                            if (it) setOnClickListener {
                                textListener(item)
                            }
                        }
                        item.position.let {
                            val alignment = when (it) {
                                Constants.CONTENT_POSITION.START -> TextView.TEXT_ALIGNMENT_TEXT_START
                                Constants.CONTENT_POSITION.END -> TextView.TEXT_ALIGNMENT_TEXT_END
                                else -> TextView.TEXT_ALIGNMENT_CENTER
                            }
                            textAlignment = alignment
                        }
                    }

                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_form, parent, false)
                )
            }

            override fun getItemCount(): Int = data.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(data[position])
            }

            fun setData(list: ArrayList<TextData>) {
                data.clear()
                data.addAll(list)
                notifyDataSetChanged()
            }

        }

        class CheckBoxAdapter(
            private val checkBoxListener: (CheckBoxData) -> Unit
        ) : RecyclerView.Adapter<CheckBoxAdapter.ViewHolder>() {
            private val data = ArrayList<CheckBoxData>()

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(item: CheckBoxData) = with(itemView) {
                    val bindData = ItemFormBinding.bind(this)
                    item.label?.let {
                        bindData.cbContent.apply {
                            setVisible(it.isNotEmpty())
                            text = it
                            setOnCheckedChangeListener { _, isChecked ->
                                item.actualValue = when (isChecked) {
                                    true -> it
                                    else -> null
                                }
                                checkBoxListener(item)
                            }
                        }
                    }
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_form, parent, false)
                )
            }

            override fun getItemCount(): Int = data.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(data[position])
            }

            fun setData(list: ArrayList<CheckBoxData>) {
                data.clear()
                data.addAll(list)
                notifyDataSetChanged()
            }

        }

        class WebViewAdapter : RecyclerView.Adapter<WebViewAdapter.ViewHolder>() {
            private val data = ArrayList<String>()

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(item: String) = with(itemView) {
                    val bindData = ItemFormBinding.bind(this)
                    bindData.webviewContent.apply {
                        setVisible(item.isNotEmpty())
                        when (item.startsWith("<")) {
                            true -> loadDataWithBaseURL(
                                null,
                                item,
                                "text/html",
                                Xml.Encoding.UTF_8.name,
                                null
                            )

                            else -> {
                                this.loadUrl(item)
                            }
                        }
                    }
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_form, parent, false)
                )
            }

            override fun getItemCount(): Int = data.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(data[position])
            }

            fun setData(list: ArrayList<String>) {
                data.clear()
                data.addAll(list)
                notifyDataSetChanged()
            }

        }

        class SelectableAdapter(
            private val cardListener: (CardData) -> Unit
        ) : RecyclerView.Adapter<SelectableAdapter.ViewHolder>() {
            private val data = ArrayList<CardData>()

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(item: CardData) = with(itemView) {
                    val bindData = ItemFormBinding.bind(this)
                    bindData.cardContainer.toShow()
                    bindData.textContainer.toShow()
                    item.title?.let {
                        bindData.tvTitle.apply {
                            text = it
                            setVisible(it.isNotEmpty())
                        }
                    }
                    item.description?.let {
                        bindData.apply {
                            tvDescription.text = it
                            setVisible(it.isNotEmpty())
                        }
                    }
                    item.price?.let {
                        bindData.tvPrice.apply {
                            toShow()
                            val priceTag = "Rp${it.thousandSeparator()}"
                            text = priceTag
                        }

                    }
                    val typedValue = TypedValue()
                    when (item.selected) {
                        true -> {
                            context.theme.resolveAttribute(
                                attr.colorTertiaryContainer,
                                typedValue,
                                true
                            )
                        }

                        else -> {
                            context.theme.resolveAttribute(
                                attr.colorPrimaryContainer,
                                typedValue,
                                true
                            )
                        }
                    }

                    val selectedBackground = ContextCompat.getColor(context, typedValue.resourceId)
                    bindData.cardConstraintContainer.setBackgroundColor(selectedBackground)

                    bindData.cardConstraintContainer.setOnClickListener {
                        item.selected = true
                        item.quantity = 1
                        cardListener(item)
                        notifyDataSetChanged()
                    }
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_form, parent, false)
                )
            }

            override fun getItemCount(): Int = data.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(data[position])
            }

            fun setData(list: ArrayList<CardData>) {
                data.clear()
                data.addAll(list)
                notifyDataSetChanged()
            }

        }

        class QuantityAdapter(
            private val cardListener: (CardData) -> Unit
        ) : RecyclerView.Adapter<QuantityAdapter.ViewHolder>() {
            private val data = ArrayList<CardData>()

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                fun bind(item: CardData) = with(itemView) {
                    val bindData = ItemFormBinding.bind(this)
                    bindData.cardContainer.toShow()
                    bindData.textContainer.toShow()
                    item.title?.let {
                        bindData.tvTitle.apply {
                            text = it
                            setVisible(it.isNotEmpty())
                        }
                    }
                    item.description?.let {
                        bindData.apply {
                            tvDescription.text = it
                            setVisible(it.isNotEmpty())
                        }
                    }
                    item.quantity.let {
                        bindData.tvInputValue.setText((it ?: 0).toString())
                    }
                    item.price?.let {
                        bindData.tvPrice.apply {
                            toShow()
                            val priceTag = "Rp${it.thousandSeparator()}"
                            text = priceTag
                        }

                    }
                    bindData.containerQuantity.toShow()
                    bindData.tvInputValue.addTextChangedListener {
                        when {
                            it?.toString()?.isEmpty() == true -> bindData.tvInputValue.setText("0")
                            it?.toString()?.isDigitsOnly() == true -> {
                                when {
                                    bindData.tvInputValue.text.toString()
                                        .getIntValue() == 0 -> minAccessible(bindData.btnMin, false)

                                    bindData.tvInputValue.text.toString()
                                        .getIntValue() == (item.limit ?: 50) -> plusAccessible(
                                        bindData.btnPlus,
                                        false
                                    )

                                    bindData.tvInputValue.text.toString().getIntValue() > 0 -> {
                                        minAccessible(bindData.btnMin, true)
                                        plusAccessible(bindData.btnPlus, true)
                                    }
                                }
                            }
                        }
                        item.quantity = bindData.tvInputValue.text.toString().getIntValue()
                        cardListener(item)
                    }
                    bindData.btnMin.setOnClickListener {
                        performReduceAmount(bindData, item)
                    }
                    bindData.btnPlus.setOnClickListener {
                        performAddAmount(bindData, item)
                    }
                }

                private fun minAccessible(btnMin: ImageButton, state: Boolean) {
                    if (btnMin.isEnabled != state) btnMin.isEnabled = state
                }

                private fun plusAccessible(btnPlus: ImageButton, state: Boolean) {
                    if (btnPlus.isEnabled != state) btnPlus.isEnabled = state
                }

                private fun performAddAmount(bindData: ItemFormBinding, item: CardData) {
                    var current = bindData.tvInputValue.text.toString().getIntValue()
                    current += 1
                    bindData.tvInputValue.setText(current.toString())
                }

                private fun performReduceAmount(bindData: ItemFormBinding, item: CardData) {
                    var current = bindData.tvInputValue.text.toString().getIntValue()
                    if (current > 0) {
                        current -= 1
                        bindData.tvInputValue.setText(current.toString())
                    }
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_form, parent, false)
                )
            }

            override fun getItemCount(): Int = data.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(data[position])
            }

            fun setData(list: ArrayList<CardData>) {
                data.clear()
                data.addAll(list)
                notifyDataSetChanged()
            }

        }

        class InputTextAdapter(
            private val recyclerView: RecyclerView,
            private val textListener: (TextData) -> Unit
        ) : RecyclerView.Adapter<InputTextAdapter.ViewHolder>() {
            private val data = ArrayList<TextData>()

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
                private var job: Job? = null

                fun bind(item: TextData) = with(itemView) {
                    val bindData = ItemFormBinding.bind(this)
                    bindData.inputLabel.apply {
                        hint = item.hint
                        toShow()
                        endIconMode = TextInputLayout.END_ICON_CLEAR_TEXT
                        counterMaxLength = item.limit
                    }
                    bindData.etInput.apply {
                        inputType = when (item.inputType) {
                            Constants.INPUT_TYPE.EMAIL -> InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                            Constants.INPUT_TYPE.NUMBER, Constants.INPUT_TYPE.PHONE -> InputType.TYPE_CLASS_NUMBER
                            else -> InputType.TYPE_CLASS_TEXT
                        }
                        doOnTextChanged { text, start, before, count ->
                            job?.cancel()
                        }
                        doAfterTextChanged {
                            if (item.inputType == Constants.INPUT_TYPE.PHONE) {
                                if (!it.isNullOrEmpty() && it.startsWith("0")) {
                                    setText(it.toString().replaceFirst("0", "+62"))
                                    setSelection(text.toString().length)
                                }
                                when (it.toString().length >= 11) {
                                    true -> {
                                        bindData.inputLabel.helperText = ""
                                        bindData.inputLabel.isErrorEnabled = false
                                    }

                                    else -> {
                                        bindData.inputLabel.helperText = "Invalid Format"
                                        bindData.inputLabel.isErrorEnabled = true
                                    }
                                }
                            }

                            job = CoroutineScope(Dispatchers.Main).launch {
                                delay(1000)
                                onTimerExpired(bindData.etInput, item)
                            }
                        }
                        setOnFocusChangeListener { _, hasFocus ->
                            if (!hasFocus) job?.cancel()
                        }
                    }
                }

                private fun onTimerExpired(etInput: TextInputEditText, item: TextData) {
                    item.text = etInput.text.toString()
                    textListener(item)
                }

                fun cancelCoroutine() {
                    job?.cancel()
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_form, parent, false)
                )
            }

            override fun getItemCount(): Int = data.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(data[position])
            }

            fun setData(list: ArrayList<TextData>) {
                data.clear()
                data.addAll(list)
                notifyDataSetChanged()
            }

            fun cancelAllCoroutines() {
                for (i in 0 until itemCount) {
                    val viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(i) as? InputTextAdapter.ViewHolder
                    viewHolder?.cancelCoroutine()
                }
            }
        }

        class FormAdapter(
            private val recyclerView: RecyclerView,
            private val actionListener: (Int?, String?, Any) -> Unit,
            private val expandListener: (FormData) -> Unit
        ) : RecyclerView.Adapter<FormAdapter.ViewHolder>() {
            private val data = ArrayList<FormData>()

            inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

                private var textAdapter: InputTextAdapter? = null
                fun bind(item: FormData) = with(itemView) {
                    val bindData = ItemContentBinding.bind(this)
                    when (item.label.isNullOrEmpty()) {
                        true -> bindData.containerHeader.toHide()
                        else -> {
                            bindData.containerHeader.toShow()
                            item.label?.let {
                                bindData.tvLabel.apply {
                                    text = it
                                    toShow()
                                }
                                bindData.containerHeader.setVisible(it.isNotEmpty())
                            }
                            item.subLabel?.let {
                                bindData.tvSubLabel.apply {
                                    setVisible(it.isNotEmpty())
                                    text = it
                                }
                            }
                        }
                    }
                    when (item.type) {
                        Constants.FORM_TYPE.CARDS -> {
                            val childAdapter = CardAdapter {
                                actionListener(item.type, item.id, it)
                            }
                            childAdapter.setData(item.cards ?: arrayListOf())
                            bindData.rvChild.apply {
                                adapter = childAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }

                        Constants.FORM_TYPE.SELECTABLE -> {
                            bindData.rvChild.apply {
                                adapter = SelectableAdapter {
                                    item.cards?.onEach { card ->
                                        card.selected = it.id == card.id
                                    }
                                }.apply {
                                    setData(item.cards ?: arrayListOf())
                                }
                                layoutManager = GridLayoutManager(context, 2)
                            }
                        }

                        Constants.FORM_TYPE.QUANTITY -> {
                            val childAdapter = QuantityAdapter {
                                actionListener(item.type, item.id, it)
                            }
                            childAdapter.setData(item.cards ?: arrayListOf())
                            bindData.rvChild.apply {
                                adapter = childAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }

                        Constants.FORM_TYPE.CHECK_BOX -> {
                            val childAdapter = CheckBoxAdapter {
                                actionListener(item.type, item.id, it)
                            }
                            childAdapter.setData(item.checkboxes ?: arrayListOf())
                            bindData.rvChild.apply {
                                adapter = childAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }

                        Constants.FORM_TYPE.WEB_VIEW -> {
                            val childAdapter = WebViewAdapter()
                            childAdapter.setData(item.webViews ?: arrayListOf())
                            bindData.rvChild.apply {
                                adapter = childAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }

                        Constants.FORM_TYPE.INPUT_TEXT -> {
                            textAdapter = InputTextAdapter(bindData.rvChild) {
                                actionListener(item.type, item.id, it)
                            }

                            textAdapter?.setData(item.texts ?: arrayListOf())
                            bindData.rvChild.apply {
                                adapter = textAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }

                        Constants.FORM_TYPE.TEXT -> {
                            val childAdapter = TextAdapter {
                                actionListener(item.type, item.id, it)
                            }
                            childAdapter.setData(item.texts ?: arrayListOf())
                            bindData.rvChild.apply {
                                adapter = childAdapter
                                layoutManager = LinearLayoutManager(context)
                            }
                        }

                        else -> {
                            // Do Nothing
                        }
                    }
                    bindData.btnExpander.setOnClickListener {
                        performExpansion(bindData, item)
                    }
                    bindData.containerHeader.setOnClickListener {
                        performExpansion(bindData, item)
                    }
                    if (bindData.rvChild.isVisible() && !item.label.isNullOrEmpty()) {
                        item.expanded = true
                        bindData.btnExpander.rotation = 180f
                    }
                }

                private fun performExpansion(bindData: ItemContentBinding, item: FormData) {
                    val iconRotationTarget = if (!bindData.rvChild.isVisible()) 180f else 0f
                    when (bindData.rvChild.isVisible()) {
                        true -> {
                            bindData.rvChild.toHide()
                            item.expanded = false
                        }

                        false -> {
                            bindData.rvChild.toShow()
                            item.expanded = true
                        }
                    }
                    bindData.btnExpander.rotation = iconRotationTarget
                    expandListener(item)
                }

                fun cancelCoroutine() {
                    textAdapter?.cancelAllCoroutines()
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
                return ViewHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.item_content, parent, false)
                )
            }

            override fun getItemCount(): Int = data.size

            override fun onBindViewHolder(holder: ViewHolder, position: Int) {
                holder.bind(data[position])
            }

            fun setData(list: ArrayList<FormData>) {
                data.clear()
                data.addAll(list)
                notifyDataSetChanged()
            }

            fun cancelChildCoroutines() {
                for (i in 0 until itemCount) {
                    val viewHolder =
                        recyclerView.findViewHolderForAdapterPosition(i) as? FormAdapter.ViewHolder
                    viewHolder?.cancelCoroutine()
                }
            }
        }
    }

    private lateinit var mContext: Context
    private lateinit var binding: LayoutFormBinding
    private var formAdapter: FormAdapter? = null
    private var formList: ArrayList<FormData>? = null
    private var listener: FormInterface? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        mContext = context
        binding = LayoutFormBinding.bind(
            LayoutInflater.from(mContext).inflate(R.layout.layout_form, this, true)
        )
        initAdapter()
    }

    private fun initAdapter() {
        formAdapter = FormAdapter(binding.rvContent,
            actionListener = { type: Int?, parentId: String?, data: Any ->
                when (type) {
                    Constants.FORM_TYPE.TEXT -> (data as TextData).let { text ->
                        listener?.onActionText(
                            text.id ?: ""
                        )
                    }

                    Constants.FORM_TYPE.CARDS -> (data as CardData).id?.let {
                        listener?.onActionCard(
                            it
                        )
                    }

                    Constants.FORM_TYPE.CHECK_BOX -> (data as CheckBoxData).let { cb ->
                        formList?.firstOrNull { it.id == parentId }?.let { child ->
                            child.checkboxes?.firstOrNull { it.id == cb.id }?.apply {
                                actualValue = cb.actualValue
                                listener?.onActionCheckBox(parentId ?: "", this)
                            }
                        }
                        formList?.let { listener?.onListChanged(it) }
                    }

                    Constants.FORM_TYPE.SELECTABLE -> {
                        (data as CardData).let { card ->
                            formList?.firstOrNull { it.id == parentId }?.apply {
                                cards?.forEach {
                                    it.selected = it.id == card.id
                                    it.total = (card.quantity ?: 0) * (card.price ?: 1.0)
                                }
                            }
                            listener?.onActionSelected(parentId ?: "", card)
                            formList?.let { listener?.onListChanged(it) }
                        }
                    }

                    Constants.FORM_TYPE.QUANTITY -> {
                        (data as CardData).let { card ->
                            formList?.firstOrNull { it.id == parentId }?.apply {
                                cards?.firstOrNull { it.id == card.id }?.let {
                                    it.quantity = card.quantity
                                    it.total = (card.quantity ?: 0) * (card.price ?: 1.0)
                                    listener?.onActionQuantity(parentId ?: "", it)
                                }
                            }
                            formList?.let { listener?.onListChanged(it) }
                        }
                    }

                    Constants.FORM_TYPE.INPUT_TEXT -> {
                        (data as TextData).let { text ->
                            formList?.firstOrNull { it.id == parentId }?.apply {
                                texts?.firstOrNull { it.id == text.id }?.let {
                                    it.text = text.text
                                    listener?.onActionInput(parentId ?: "", text)
                                }
                            }
                            formList?.let { listener?.onListChanged(it) }
                        }
                    }

                    else -> {
                        // Do Nothing
                    }
                }
            }, expandListener = { formData: FormData ->
                formList?.firstOrNull { it.id == formData.id }?.apply {
                    expanded = formData.expanded
                }
                listener?.onDataChanged(formData)
            }
        )

        binding.rvContent.apply {
            adapter = formAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    fun setData(list: ArrayList<FormData>) {
        list.forEach {
            Log.d("HAHA", "$it")
        }
        formList = list
        formAdapter?.setData(list)
    }

    fun getData() = formList
    fun getDataById(id: String? = null) = formList?.firstOrNull { it.id == id }

    fun cancelCoroutines() {
        formAdapter?.cancelChildCoroutines()
    }

    fun setListener(formListener: FormInterface) {
        listener = formListener
    }

    interface FormInterface {
        fun onDataChanged(data: FormData) {}
        fun onActionText(id: String) {}
        fun onActionCard(id: String) {}
        fun onActionCheckBox(parentId: String, checkBoxData: CheckBoxData) {}
        fun onActionSelected(parentId: String, selectedData: CardData) {}
        fun onActionQuantity(parentId: String, quantity: CardData) {}
        fun onActionInput(parentId: String, textData: TextData) {}
        fun onListChanged(list: ArrayList<FormData>) {}
    }
}