package com.rogie.threekingdoms.model

import com.rogie.threekingdoms.meta.CharacterId

data class StoryOption(
    val text: String,
    val resultText: String,
    val triggerFight: Boolean = false,
    val forcedEnemy: Enemy? = null,
    val onChoice: (Player) -> Unit
)

data class StoryEvent(
    val id: String,
    val title: String,
    val description: String,
    val options: List<StoryOption>
)

object StoryLibrary {
    fun getBossEvent(enemy: Enemy): StoryEvent {
        return when (enemy.id) {
            "BOSS_TAOTIE" -> StoryEvent(
                "BOSS_TAOTIE_EVENT",
                "เผชิญหน้าอสูรตะกละ Taotie",
                "เงาทมิฬขนาดมหึมากลืนกินแสงสว่างจนหมดสิ้น ปากที่ไม่มีวันอิ่มของมันขยับเขยื้อน 'เจ้ามาเพื่อเติมเต็มความหิวโหยของข้าหรือ?'",
                listOf(
                    StoryOption("มอบวิญญาณส่วนหนึ่งให้ (เสีย 1 Max HP)", "มันหัวเราะเยาะเย้ย 'ช่างโอชะ... แต่ยังไม่พอ!'", true, enemy, { it.maxHp -= 1; it.hp = it.hp.coerceAtMost(it.maxHp) }),
                    StoryOption("ท้าทายความหิวโหยของมัน", "ท่านชักดาบออกมากระตุ้นความโกรธเกรี้ยวของอสูร!", true, enemy, { it.strength += 1 }),
                    StoryOption("ใช้เศษอาหารล่อลวง (เสีย 30 ทอง)", "มันงับเหยื่อล่ออย่างตะกละตะกลาม เปิดโอกาสให้ท่านโจมตีก่อน", true, enemy, { it.gold -= 30; /* First strike logic placeholder */ })
                )
            )
            "BOSS_LU_BU" -> StoryEvent(
                "BOSS_LU_BU_EVENT",
                "การเผชิญหน้ากับลิโป้ (Overlord)",
                "บนหลังม้าเซ็กเธาว์ที่สูงตระหง่าน ลิโป้มองลงมาด้วยสายตาที่ดูแคลน 'เจ้ายังไม่ตายอีกหรือ? ข้าจะสงเคราะห์ให้เอง!'",
                listOf(
                    StoryOption("ท้าทายด้วยเกียรติ", "ลิโป้หัวเราะกึกก้อง 'ข้าชอบใจในความบ้าคลั่งของเจ้า!'", true, enemy, { it.strength += 1 }),
                    StoryOption("วางแผนซุ่มโจมตี", "ท่านเตรียมท่าตั้งรับอย่างรัดกุม", true, enemy, { it.block += 1 }),
                    StoryOption("แสดงความหวาดกลัว", "ลิโป้พุ่งเข้าหาด้วยความเร็วที่เหนือมนุษย์!", true, enemy, { it.energy = (it.energy - 1).coerceAtLeast(0) })
                )
            )
            else -> StoryEvent(
                "BOSS_GENERIC",
                "ศัตรูที่แข็งแกร่งปรากฏกาย",
                "แม่ทัพฝ่ายศัตรูยืนตระหง่านอยู่เบื้องหน้า 'ที่นี่จะเป็นสุสานของเจ้า!'",
                listOf(
                    StoryOption("เข้าปะทะทันที", "ดาบของท่านปะทะกับศัตรูอย่างรุนแรง!", true, enemy, { }),
                    StoryOption("วิเคราะห์จุดอ่อน", "ท่านมองเห็นช่องโหว่ในการป้องกัน", true, enemy, { it.speed += 1 })
                )
            )
        }
    }

    fun getRandomEvent(): StoryEvent {
        val events = listOf(
            StoryEvent(
                "GATE_GUARD",
                "ด่านตรวจชายแดน",
                "ทหารยามกลุ่มหนึ่งขวางทางท่านไว้และเรียกเก็บภาษีผ่านทางที่ขูดรีด",
                listOf(
                    StoryOption("จ่ายสินบน (20 ทอง)", "ทหารยามปล่อยท่านไปอย่างรวดเร็ว", false, null, { it.gold -= 20 }),
                    StoryOption("บุกฝ่าด่าน", "ท่านชักอาวุธออกมาและเตรียมบุกทะลวง!", true, Enemy("GUARD", "ทหารยามด่าน", 8, 8, 1), { }),
                    StoryOption("ใช้ปัญญาเจรจา", "ทหารยามลังเล แต่สุดท้ายก็ลดค่าผ่านทางครึ่งหนึ่ง", false, null, { it.gold -= 10 })
                )
            ),
            StoryEvent(
                "AMBUSH",
                "ซุ่มโจมตีในหุบเขา",
                "ขณะที่ท่านเดินทางผ่านหุบเขาแคบๆ ท่านรู้สึกถึงสายตาที่จ้องมองมาจากความมืด",
                listOf(
                    StoryOption("ตั้งรับอย่างระมัดระวัง", "ท่านเตรียมพร้อมรับมือกับการโจมตี", true, Enemy("BANDITS", "โจรดักซุ่ม", 5, 5, 1), { it.block += 1 }),
                    StoryOption("เร่งฝีเท้าผ่านไป", "ท่านพยายามควบม้าหนี แต่ถูกขวางหน้าไว้", true, Enemy("BANDITS", "โจรดักซุ่ม", 5, 5, 1), { it.hp -= 1 }),
                    StoryOption("ส่งเสียงตะโกนข่มขวัญ", "พวกโจรตกใจกลัวและเผยตัวออกมาต่อสู้", true, Enemy("BANDITS", "โจรดักซุ่ม", 5, 5, 1), { it.strength += 1 })
                )
            )
        )
        return events.random()
    }
}
