package com.rogie.threekingdoms.ui

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.model.EquipmentType

class InventoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inventory)

        val tabLayout = findViewById<TabLayout>(R.id.tabInvCategories)
        
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val type = when(tab?.position) {
                    0 -> EquipmentType.WEAPON
                    1 -> EquipmentType.ARMOR
                    2 -> EquipmentType.MOUNT
                    else -> EquipmentType.TREASURE
                }
                refreshList(type)
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        findViewById<Button>(R.id.btnCloseInv).setOnClickListener {
            finish()
        }

        refreshList(EquipmentType.WEAPON)
    }

    private fun refreshList(type: EquipmentType) {
        val items = GameSession.inventory.filter { it.type == type }
        // Setup RecyclerView adapter here when items are available
    }
}
