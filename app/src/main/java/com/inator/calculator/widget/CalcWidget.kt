package com.inator.calculator.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.inator.calculator.R
import com.inator.calculator.repository.toExpression
import com.inator.calculator.repository.toSimpleString
import org.mariuszgromada.math.mxparser.Expression

class CalcWidget : AppWidgetProvider() {
    private val actions = listOf(
        "ZERO",
        "ONE",
        "TWO",
        "THREE",
        "FOUR",
        "FIVE",
        "SIX",
        "SEVEN",
        "EIGHT",
        "NINE",
        "DECIMAL",
        "ADD",
        "SUBTRACT",
        "DIVIDE",
        "MULTIPLY",
        "BACKSPACE",
        "EQUAL"
    )
    private val inputs =
        listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ".", "+", "-", "%", "×")
    private lateinit var remoteViews: RemoteViews
    override fun onUpdate(
        context: Context, appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (widgetId in appWidgetIds) {
            remoteViews = RemoteViews(context.packageName, R.layout.widget_simple_calc)
            setUpIntent(context, remoteViews, "ZERO", R.id.widget_button_0)
            setUpIntent(context, remoteViews, "ONE", R.id.widget_button_1)
            setUpIntent(context, remoteViews, "TWO", R.id.widget_button_2)
            setUpIntent(context, remoteViews, "THREE", R.id.widget_button_3)
            setUpIntent(context, remoteViews, "FOUR", R.id.widget_button_4)
            setUpIntent(context, remoteViews, "FIVE", R.id.widget_button_5)
            setUpIntent(context, remoteViews, "SIX", R.id.widget_button_6)
            setUpIntent(context, remoteViews, "SEVEN", R.id.widget_button_7)
            setUpIntent(context, remoteViews, "EIGHT", R.id.widget_button_8)
            setUpIntent(context, remoteViews, "NINE", R.id.widget_button_9)
            setUpIntent(context, remoteViews, "DECIMAL", R.id.widget_button_point)
            setUpIntent(context, remoteViews, "ADD", R.id.add_button_widget)
            setUpIntent(context, remoteViews, "SUBTRACT", R.id.sub_button_widget)
            setUpIntent(context, remoteViews, "MULTIPLY", R.id.mul_button_widget)
            setUpIntent(context, remoteViews, "DIVIDE", R.id.divide_button_widget)
            setUpIntent(context, remoteViews, "BACKSPACE", R.id.clear_button_widget)
            setUpIntent(context, remoteViews, "EQUAL", R.id.equal_button_widget)
            appWidgetManager.updateAppWidget(widgetId, remoteViews)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        var input = getInput(context)
        val action = intent.action
        if (actions.contains(action)) {
            input = if (action == "BACKSPACE") {
                if (getAllClear(context)) {
                    ""
                } else {
                    setAllClear(context, false)
                    input?.substring(0, input.length - 1)
                }
            } else if (action == "EQUAL") {
                setAllClear(context, true)
                val result = input?.let { Expression(it.toExpression()).calculate() }
                result?.toSimpleString()
            } else {
                setAllClear(context, false)
                input + inputs[actions.indexOf(action)]
            }
            val appWidgetManager = AppWidgetManager.getInstance(context)
            val appWidgetIds =
                appWidgetManager.getAppWidgetIds(ComponentName(context, CalcWidget::class.java))
            for (widgetId in appWidgetIds) {
                remoteViews = RemoteViews(context.packageName, R.layout.widget_simple_calc)
                remoteViews.setTextViewText(R.id.output_widget, input)
                if (getAllClear(context)) {
                    remoteViews.setTextViewText(R.id.clear_button_widget, "C")
                } else {
                    remoteViews.setTextViewText(R.id.clear_button_widget, "⌫")
                }
                appWidgetManager.updateAppWidget(widgetId, remoteViews)
            }
            setInput(input, context)
        }
        super.onReceive(context, intent)
    }

    private fun setUpIntent(context: Context?, remoteViews: RemoteViews, action: String?, id: Int) {
        val intent = Intent(context, CalcWidget::class.java)
        intent.action = action
        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)
        remoteViews.setOnClickPendingIntent(id, pendingIntent)
    }

    private fun setInput(input: String?, context: Context) {
        val preferences = context.getSharedPreferences("WidgetInput", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putString("INPUT", input)
        editor.apply()
    }

    private fun getInput(context: Context): String? {
        val preferences = context.getSharedPreferences("WidgetInput", Context.MODE_PRIVATE)
        return preferences.getString("INPUT", "")
    }

    private fun setAllClear(context: Context, allClear: Boolean) {
        val preferences = context.getSharedPreferences("WidgetInput", Context.MODE_PRIVATE)
        val editor = preferences.edit()
        editor.putBoolean("AllClear", allClear)
        editor.apply()
    }

    private fun getAllClear(context: Context): Boolean {
        val preferences = context.getSharedPreferences("WidgetInput", Context.MODE_PRIVATE)
        return preferences.getBoolean("AllClear", false)
    }
}