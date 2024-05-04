package com.omarea.krscript.ui

import android.content.Context
import android.view.ViewGroup
import com.omarea.krscript.R
import com.omarea.krscript.model.GroupNode

open class ListItemGroup(context: Context,
                         var isRootGroup: Boolean,
                         config: GroupNode) :
        ListItemView(
                context,
                if (isRootGroup) R.layout.kr_group_list_root else R.layout.kr_group_list_item,
                config) {
    private var children = ArrayList<ListItemView>()

    fun addView(item: ListItemView): ListItemGroup {
        layout.findViewById<ViewGroup>(android.R.id.content).addView(item.getView())
        children.add(item)

        return this
    }

    fun triggerActionByKey(key: String): Boolean {
        for (child in this.children) {
            if (child is ListItemClickable && child.key == key) {
                child.triggerAction()
                return true
            } else if (child is ListItemGroup && child.triggerActionByKey(key)) {
                return true
            }
        }
        return false
    }

    fun triggerUpdateByKey(keys: Array<String>) {
        for (key in keys) {
            if (key == this.key) {
                triggerUpdate()
            } else {
                for (child in this.children) {
                    if (child is ListItemGroup) {
                        child.triggerUpdateByKey(keys)
                    } else if (child.key == key) {
                        child.updateViewByShell()
                    }
                }
            }
        }
    }

    fun triggerUpdate() {
        for (child in this.children) {
            if (child is ListItemGroup) {
                child.triggerUpdate()
            } else {
                child.updateViewByShell()
            }
        }
    }

    init {
        title = config.title
    }
}
