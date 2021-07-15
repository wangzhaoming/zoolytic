package org.zoolytic.plugin

import com.intellij.openapi.diagnostic.Logger
import java.util.*
import javax.swing.table.DefaultTableModel
import javax.swing.tree.DefaultMutableTreeNode

class TableModel : DefaultTableModel() {
    private val LOG = Logger.getInstance(this.javaClass)
    private var editable = false;

    fun init() {
        addColumn("Property")
        addColumn("value")
    }

    override fun isCellEditable(row: Int, column: Int) = editable && row == 1 && column == 1

    fun updateDetails(node: DefaultMutableTreeNode) {
        dataVector.clear()
        if (node is ZkRootTreeNode) {
            editable = false
            node.getNodeData()
            addRow(arrayOf("Url", node.getNodeData().text))
        } else if (node is ZkTreeNode) {
            editable = true
            arrayOf("Path", "Data", "czxid", "mzxid", "ctime", "mtime", "version", "cversion", "aversion", "ephemeralOwner",
            "dataLength", "numChildren", "pzxid").forEach { addRow(arrayOf(it, "")) }
            val nodeData = node.getNodeData()
            with(nodeData) {
                setValueAt(getFullPath(), 0, 1)
                LOG.info("reading data")
                setValueAt(data?.let { String(it) } ?: "", 1, 1)
                LOG.info("reading data2")
                if (stat == null) {
                    (2..12).forEach { setValueAt("", it, 1) }
                } else {
                    setValueAt(stat?.czxid, 2, 1)
                    setValueAt(stat?.mzxid, 3, 1)
                    setValueAt(Date(stat!!.ctime), 4, 1)
                    setValueAt(Date(stat!!.mtime), 5, 1)
                    setValueAt(stat?.version, 6, 1)
                    setValueAt(stat?.cversion, 7, 1)
                    setValueAt(stat?.aversion, 8, 1)
                    setValueAt(stat?.ephemeralOwner, 9, 1)
                    setValueAt(stat?.dataLength, 10, 1)
                    setValueAt(stat?.numChildren, 11, 1)
                    setValueAt(stat?.pzxid, 12, 1)
                }
                LOG.info("reading data10")
            }
        }
    }
}