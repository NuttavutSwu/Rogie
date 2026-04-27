package com.rogie.threekingdoms.model

data class SecretBossReward(
    val unlockHiddenPaths: Boolean = false,
    val insightRelic: Boolean = false,
    val fireResistance: Boolean = false,
    val controlBuff: Boolean = false,
    val fireDamageRelic: Boolean = false,
    val damageRelic: Boolean = false,
    val healBuff: Boolean = false,
    val sustainBonus: Boolean = false,
    val poisonDamage: Boolean = false,
    val unlockLuBuAlly: Boolean = false,
    val legendaryWeaponUpgrade: Boolean = false
)

data class BossDialogue(
    val intro: String,
    val midBattle: String,
    val defeat: String,
    val playerResponses: List<String>,
    val branchDialogues: Map<Int, String> = emptyMap(),
    val postChoiceDialogues: Map<String, String> = emptyMap() // "SPARE" or "EXECUTE"
)

data class SecretBoss(
    val id: String,
    val name: String,
    val title: String,
    val sceneEntry: String,
    val personality: String,
    val baseHp: Int,
    val baseDamage: Int,
    val speed: Int,
    val mechanicDescription: String,
    val unlockCondition: String,
    val loreHint: String,
    val dialogue: BossDialogue,
    val spareReward: SecretBossReward,
    val executeReward: SecretBossReward
)

object SecretBossLibrary {
    val bosses = listOf(
        SecretBoss(
            id = "SECRET_PHANTOM",
            name = "Phantom Strategist",
            title = "เงาแห่งนักปราชญ์",
            sceneEntry = "ท่านเลือกความเมตตา... แต่ท่านกำลังก้าวเข้าสู่สมรภูมิที่มองไม่เห็น",
            personality = "ลึกลับและเปี่ยมด้วยปัญญา",
            baseHp = 15,
            baseDamage = 1,
            speed = 12,
            mechanicDescription = "กลลวงตา: สลับการ์ดในมือทุก 3 เทิร์น",
            unlockCondition = "บทที่ 1: สำรวจซากปรักหักพัง และ ไว้ชีวิตบอส",
            loreHint = "กลยุทธ์ที่ไร้ซึ่งความเข้าใจคือความว่างเปล่า",
            dialogue = BossDialogue(
                intro = "ท่านเชื่อจริงๆ หรือว่าการไว้ชีวิตคนเพียงคนเดียวจะช่วยคนได้มากมาย? หรือมันเป็นเพียงการประวิงเวลาแห่งความโกลาหล?",
                midBattle = "กลยุทธ์ที่ไร้ซึ่งความเข้าใจคือความว่างเปล่า",
                defeat = "ดูเหมือนท่านจะมีความเข้าใจมากกว่าที่ข้าคิด...",
                playerResponses = listOf("ข้าแสวงหาสมดุล", "ข้าจะยุติความโกลาหลด้วยกำลัง"),
                branchDialogues = mapOf(0 to "งั้นจงพิสูจน์ปัญญาของท่าน", 1 to "งั้นท่านก็ไม่ต่างจากคนอื่น"),
                postChoiceDialogues = mapOf("SPARE" to "ไม่ใช่อุบายทุกอย่างที่ต้องการชัยชนะ", "EXECUTE" to "แม้แต่ปัญญาก็พ่ายแพ้ต่อคมดาบ")
            ),
            spareReward = SecretBossReward(unlockHiddenPaths = true, insightRelic = true),
            executeReward = SecretBossReward(damageRelic = true)
        ),
        SecretBoss(
            id = "SECRET_QILIN",
            name = "Infernal Qilin",
            title = "กิเลนอเวจี",
            sceneEntry = "ท่านหล่อเลี้ยงเพลิงแห่งสงคราม... บัดนี้จงเผชิญหน้ากับมัน",
            personality = "ดุร้ายและทรงพลัง",
            baseHp = 20,
            baseDamage = 2,
            speed = 8,
            mechanicDescription = "เพลิงอเวจี: สะท้อนดาเมจเป็นสถานะ Burn",
            unlockCondition = "บทที่ 2: สังหารบอสก่อนหน้า และ Chaos >= 2",
            loreHint = "แม้แต่ไฟก็มอดดับได้",
            dialogue = BossDialogue(
                intro = "เส้นทางของท่านอบอวลไปด้วยกลิ่นอายแห่งการทำลายล้าง... ท่านจะมอดไหม้ไปกับมัน หรือจะก้าวข้ามมันไป?",
                midBattle = "ความร้อนนี้จะแผดเผาวิญญาณที่แปดเปื้อนของท่าน!",
                defeat = "แม้แต่ไฟ... ก็มอดดับได้...",
                playerResponses = listOf("ข้าจะดับไฟนี้", "ข้าจะกลายเป็นไฟที่กลืนกินทุกสิ่ง"),
                postChoiceDialogues = mapOf("SPARE" to "ท่านเลือกที่จะยับยั้งชั่งใจ", "EXECUTE" to "จงกลายเป็นเปลวเพลิงที่กลืนกินทุกสิ่ง")
            ),
            spareReward = SecretBossReward(fireResistance = true, controlBuff = true),
            executeReward = SecretBossReward(fireDamageRelic = true)
        ),
        SecretBoss(
            id = "SECRET_SERPENT",
            name = "White Serpent Spirit",
            title = "จิตวิญญาณนางพญางูขาว",
            sceneEntry = "จิตใจที่อ่อนโยนก้าวเข้าสู่สถานที่อันตราย",
            personality = "นิ่งสงบและสังเกตการณ์",
            baseHp = 18,
            baseDamage = 1,
            speed = 13,
            mechanicDescription = "มนต์นางพญา: ลดความเสียหายที่ได้รับ 50% หากผู้เล่นไม่มีเกราะ",
            unlockCondition = "บทที่ 3: ไว้ชีวิตบอส และ ปลดล็อกเส้นทางลับ",
            loreHint = "ความเมตตานั้นหายาก... แต่เปราะบาง",
            dialogue = BossDialogue(
                intro = "ความเมตตานั้นหายาก... แต่เปราะบาง ท่านจะปกป้องมันได้หรือไม่?",
                midBattle = "บางทีท่านอาจจะต่างออกไป...",
                defeat = "จิตใจของท่าน... แข็งแกร่งกว่าที่ข้าเห็น...",
                playerResponses = listOf("ข้าจะปกป้องความดีงาม", "โลกนี้ไม่มีที่ว่างให้คนอ่อนแอ"),
                postChoiceDialogues = mapOf("SPARE" to "จงรักษาความเมตตานั้นไว้", "EXECUTE" to "แม้แต่ความเมตตาก็มีขีดจำกัด")
            ),
            spareReward = SecretBossReward(healBuff = true, sustainBonus = true),
            executeReward = SecretBossReward(poisonDamage = true)
        ),
        SecretBoss(
            id = "SECRET_SHADOW_LUBU",
            name = "Shadow Lu Bu",
            title = "เงาแห่งลิโป้",
            sceneEntry = "ความเมตตาคือความอ่อนแอ",
            personality = "หยิ่งยโสและบ้าคลั่ง",
            baseHp = 22,
            baseDamage = 2,
            speed = 15,
            mechanicDescription = "ท้าทายสวรรค์: เพิ่มพลังโจมตีทุกครั้งที่เสียเลือด",
            unlockCondition = "บทที่ 4: ไว้ชีวิตลิโป้",
            loreHint = "ความแข็งแกร่งที่ไร้จุดมุ่งหมายนั้นไร้ค่า",
            dialogue = BossDialogue(
                intro = "ท่านไว้ชีวิตข้า... แต่ท่านกลับแสวงหาความแข็งแกร่งงั้นหรือ?",
                midBattle = "ความแข็งแกร่งที่ไร้จุดมุ่งหมายนั้นไร้ค่า!",
                defeat = "ในที่สุด... ข้าก็ได้พบจุดมุ่งหมาย...",
                playerResponses = listOf("ข้าต้องการเข้าใจท่าน", "ข้าต้องการเพียงพลังของท่าน"),
                postChoiceDialogues = mapOf("SPARE" to "ท่านเลือกความเข้าใจ", "EXECUTE" to "จงรับพลังที่ท่านโหยหาไป")
            ),
            spareReward = SecretBossReward(unlockLuBuAlly = true),
            executeReward = SecretBossReward(legendaryWeaponUpgrade = true)
        ),
        SecretBoss(
            id = "SECRET_FATE_DRAGON",
            name = "Dragon of Fate",
            title = "มังกรแห่งโชคชะตา",
            sceneEntry = "ท่านได้เปลี่ยนแปลงกงล้อแห่งพรหมลิขิตแล้ว",
            personality = "ศักดิ์สิทธิ์และอยู่เหนือทุกสรรพสิ่ง",
            baseHp = 30,
            baseDamage = 2,
            speed = 20,
            mechanicDescription = "กงล้อแห่งชะตา: สุ่มบัฟ/เดบัฟทุกเทิร์น",
            unlockCondition = "Spare >= 3 และ ชนะ Secret Boss >= 2",
            loreHint = "ชะตากรรมไม่ใช่สิ่งตายตัวอีกต่อไป",
            dialogue = BossDialogue(
                intro = "ความเมตตา ความโกลาหล พลัง... ท่านแบกรับมันไว้ทั้งหมด",
                midBattle = "ชะตากรรมไม่ใช่สิ่งตายตัวอีกต่อไป!",
                defeat = "กาลเวลาบทใหม่... กำลังจะเริ่มขึ้น...",
                playerResponses = listOf("รักษาสมดุล", "เขียนชะตาใหม่"),
                postChoiceDialogues = mapOf("SPARE" to "ท่านเลือกความสามัคคี (Ending: Peace)", "EXECUTE" to "ท่านกลายเป็นโชคชะตาบทใหม่ (Ending: Ruler)")
            ),
            spareReward = SecretBossReward(),
            executeReward = SecretBossReward()
        )
    )

    fun getById(id: String) = bosses.find { it.id == id }
}
