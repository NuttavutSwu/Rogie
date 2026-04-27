package com.rogie.threekingdoms.model

data class BossDialogue(
    val intro: String,
    val midBattle: String,
    val defeat: String,
    val playerResponses: List<String>
)

data class SecretBoss(
    val id: String,
    val name: String,
    val title: String,
    val personality: String,
    val baseHp: Int,
    val baseDamage: Int,
    val speed: Int,
    val mechanicDescription: String,
    val unlockCondition: String,
    val loreHint: String,
    val dialogue: BossDialogue
)

object SecretBossLibrary {
    val bosses = listOf(
        SecretBoss(
            id = "BOSS_QINGLONG",
            name = "Qinglong",
            title = "มังกรฟ้าแห่งทิศตะวันออก",
            personality = "เคร่งขรึม ยึดมั่นในสมดุลและชะตากรรม",
            baseHp = 15,
            baseDamage = 2,
            speed = 12,
            mechanicDescription = "ตาชั่งแห่งสมดุล: ดาเมจที่เกิน 5 ในเทิร์นเดียวจะสะท้อนกลับ",
            unlockCondition = "จบเกมโดยไม่ทำลายการ์ดเริ่มต้น",
            loreHint = "เขารอสังหารท่านในทุกกงล้อ เพราะท่านคือตัวตนที่ไม่ควรมีอยู่",
            dialogue = BossDialogue(
                intro = "เจ้าเดินสวนทางกับกระแสชะตามานับครั้งไม่ถ้วน... ถึงเวลาต้องหยุดแล้ว",
                midBattle = "เจ้ายังดิ้นรนในกรงขังที่ชื่อว่า 'เวลา' อีกหรือ?",
                defeat = "แม้ชะตาจะหักสะบั้น... แต่ความว่างเปล่ายังรอเจ้าอยู่...",
                playerResponses = listOf("ยอมรับชะตา", "ปฏิเสธชะตา")
            )
        ),
        SecretBoss(
            id = "BOSS_ZHUQUE",
            name = "Zhuque",
            title = "วิหคเพลิงแห่งทิศใต้",
            personality = "บ้าคลั่ง หลงใหลในการทำลายเพื่อจุติใหม่",
            baseHp = 12,
            baseDamage = 2,
            speed = 14,
            mechanicDescription = "เพลิงอมตะ: ฟื้นคืนชีพ 50% HP พร้อมเผาไหม้การ์ดในมือผู้เล่น",
            unlockCondition = "ชนะ 3 ศึกติดต่อกันด้วยเลือดเพียง 1 HP",
            loreHint = "ทุกครั้งที่ท่านตาย นางคือผู้ที่เก็บเถ้าถ่านวิญญาณของท่านมาหลอมใหม่",
            dialogue = BossDialogue(
                intro = "มาเถิด... มาเป็นเชื้อไฟให้กับการจุติใหม่ของข้า!",
                midBattle = "ความเจ็บปวดคือหลักฐานเดียวว่าเจ้ายังหายใจ!",
                defeat = "เถ้าถ่าน... จะไม่มีวัน... มอดไหม้...",
                playerResponses = listOf("กรีดร้องด้วยความเจ็บปวด", "ยืนหยัดในกองเพลิง")
            )
        ),
        SecretBoss(
            id = "BOSS_BAIHU",
            name = "Baihu",
            title = "พยัคฆ์ขาวแห่งทิศตะวันตก",
            personality = "เพชฌฆาตเงียบ เชี่ยวชาญการสังหาร",
            baseHp = 14,
            baseDamage = 3,
            speed = 15,
            mechanicDescription = "ลางสังหาร: หากเลือดผู้เล่นต่ำกว่า 40% จะใช้ท่าไม้ตายดาเมจมหาศาล",
            unlockCondition = "สังหารทหารธรรมดาเกิน 100 ศพ",
            loreHint = "เขาคือความผิดบาปที่ท่านก่อไว้ในอดีต ที่ตามมาทวงคืนในร่างพยัคฆ์",
            dialogue = BossDialogue(
                intro = "บาปของเจ้าหนาเกินกว่าจะเยียวยา... ข้าจะช่วยสงเคราะห์เอง",
                midBattle = "เจ้าเริ่มช้าลงแล้ว... ข้าเห็นคอของเจ้าชัดขึ้น",
                defeat = "ดาบที่คมที่สุด... คือดาบที่สังหารความหวัง...",
                playerResponses = listOf("ยอมรับความผิด", "สู้จนตัวตาย")
            )
        ),
        SecretBoss(
            id = "BOSS_XUANWU",
            name = "Xuanwu",
            title = "เต่าดำแห่งทิศเหนือ",
            personality = "เยือกเย็น อดทน เป็นดั่งปราการที่ไม่มีวันทลาย",
            baseHp = 20,
            baseDamage = 1,
            speed = 5,
            mechanicDescription = "ปราการนิรันดร์: เปลี่ยนดาเมจที่ได้รับทั้งหมดเป็นเกราะในเทิร์นถัดไป",
            unlockCondition = "มีเกราะรวมเกิน 50 ในหนึ่งการต่อสู้",
            loreHint = "ความทรงจำของแผ่นดินถูกเก็บไว้ภายใต้กระดองนี้ รวมถึงความจริงที่ท่านลืมไป",
            dialogue = BossDialogue(
                intro = "กาลเวลาไม่มีความหมายต่อหน้าข้า... ความพยายามของเจ้าก็เช่นกัน",
                midBattle = "ภูผาไม่สั่นคลอนเพราะลมพัด ดาบของเจ้าก็เป็นเพียงสายลม",
                defeat = "นิรันดร์กาล... กำลังจะสิ้นสุดลงหรือ...",
                playerResponses = listOf("ทำลายกระดอง", "รอคอยอย่างอดทน")
            )
        ),
        SecretBoss(
            id = "BOSS_TAOTIE",
            name = "Taotie",
            title = "อสูรตะกละผู้กลืนกินแสง",
            personality = "หิวโหยตลอดเวลา ไร้ก้นบึ้ง",
            baseHp = 18,
            baseDamage = 2,
            speed = 8,
            mechanicDescription = "ความหิวโหยไม่สิ้นสุด: เพิ่มพลังโจมตีตามจำนวนไอเทมที่ผู้เล่นมี",
            unlockCondition = "สะสมทองเกิน 200 โดยไม่ใช้จ่าย",
            loreHint = "ความโลภของมนุษย์หล่อเลี้ยงมัน และท่านคืออาหารที่หอมหวานที่สุด",
            dialogue = BossDialogue(
                intro = "เจ้ามาเพื่อเติมเต็มความหิวโหยของข้าหรือ?",
                midBattle = "ข้ายังไม่อิ่ม... ข้าต้องการ... วิญญาณของเจ้า!",
                defeat = "ความหิว... มันกลับมา... อีกแล้ว...",
                playerResponses = listOf("มอบวิญญาณสังเวย", "ท้าทายความหิว")
            )
        )
    )

    fun getById(id: String) = bosses.find { it.id == id }
}
