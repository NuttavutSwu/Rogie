package com.rogie.threekingdoms.model

data class StoryOption(
    val text: String,
    val resultText: String,
    val triggerFight: Boolean = false,
    val onChoice: (Player) -> Unit
)

data class StoryEvent(
    val id: String,
    val title: String,
    val description: String,
    val options: List<StoryOption>
)

object StoryLibrary {
    fun getRandomEvent(): StoryEvent {
        val events = listOf(
            StoryEvent(
                "GATE_GUARD",
                "ด่านตรวจชายแดน",
                "ทหารยามกลุ่มหนึ่งขวางทางท่านไว้และเรียกเก็บภาษีผ่านทางที่ขูดรีด",
                listOf(
                    StoryOption("จ่ายสินบน (20 ทอง)", "ทหารยามปล่อยท่านไปอย่างรวดเร็ว", false, { it.gold -= 20 }),
                    StoryOption("บุกฝ่าด่าน", "ท่านชักอาวุธออกมาและเตรียมบุกทะลวง!", true, { }),
                    StoryOption("ใช้ปัญญาเจรจา", "ทหารยามลังเล แต่สุดท้ายก็ลดค่าผ่านทางครึ่งหนึ่ง", false, { it.gold -= 10 })
                )
            ),
            StoryEvent(
                "AMBUSH",
                "ซุ่มโจมตีในหุบเขา",
                "ขณะที่ท่านเดินทางผ่านหุบเขาแคบๆ ท่านรู้สึกถึงสายตาที่จ้องมองมาจากความมืด",
                listOf(
                    StoryOption("ตั้งรับอย่างระมัดระวัง", "ท่านเตรียมพร้อมรับมือกับการโจมตี", true, { it.block += 1 }),
                    StoryOption("เร่งฝีเท้าผ่านไป", "ท่านพยายามควบม้าหนี แต่ถูกขวางหน้าไว้", true, { it.hp -= 1 }),
                    StoryOption("ส่งเสียงตะโกนข่มขวัญ", "พวกโจรตกใจกลัวและเผยตัวออกมาต่อสู้", true, { it.strength += 1 })
                )
            ),
            StoryEvent(
                "VILLAGE_HELP",
                "หมู่บ้านที่ถูกปล้น",
                "ท่านเดินทางมาถึงหมู่บ้านแห่งหนึ่งที่เพิ่งถูกกองโจรบุกปล้น ชาวบ้านขอความช่วยเหลือจากท่าน",
                listOf(
                    StoryOption("มอบเงินช่วยเหลือ (10 ทอง)", "ชาวบ้านซาบซึ้งในความเมตตาและมอบเครื่องรางคุ้มครองให้ท่าน", false, { it.gold -= 10; it.maxHp += 1; it.hp += 1 }),
                    StoryOption("ขับไล่กองโจรที่เหลือ", "ท่านมุ่งหน้าไปยังรังโจรเพื่อทวงคืนความยุติธรรม", true, { }),
                    StoryOption("เดินทางต่อโดยไม่สนใจ", "ท่านเลือกที่จะมุ่งหน้าสู่เป้าหมายต่อไป", false, { })
                )
            )
        )
        return events.random()
    }
}
