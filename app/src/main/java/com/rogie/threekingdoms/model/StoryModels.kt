package com.rogie.threekingdoms.model

import com.rogie.threekingdoms.R
import com.rogie.threekingdoms.game.GameSession
import com.rogie.threekingdoms.game.ShopLibrary
import kotlin.random.Random

data class StoryOption(
    val text: String,
    val resultText: String,
    val triggerFight: Boolean = false,
    val forcedEnemy: Enemy? = null,
    val onChoice: (Player) -> Unit,
    val nextChapter: Int? = null,
    val isExecute: Boolean = false,
    val isSpare: Boolean = false,
    val firstTurnBonus: Boolean = false
)

data class StoryEvent(
    val id: String,
    val title: String,
    val description: String,
    val options: List<StoryOption>,
    val imageRes: Int = R.drawable.img_event_generic
)

object StoryLibrary {

    // --- 1. บทนำ (Chapter Start) ---
    fun getChapterStart(chapter: Int): StoryEvent {
        return when (chapter) {
            1 -> StoryEvent(
                "CH1_START", "บทที่ 1: หมู่บ้านแห่งธุลี",
                "หมู่บ้านที่เคยสงบสุขบัดนี้เหลือเพียงซากปรักหักพัง เสียงนกร้องถูกแทนที่ด้วยเสียงกรีดร้องของวิญญาณที่วนเวียนอยู่ในหมอกหนา ท่านจะทำอย่างไร?",
                listOf(StoryOption("เริ่มการสำรวจ", "ท่านก้าวเข้าสู่เงามืดของหมู่บ้าน...", false, null, { })),
                R.drawable.img_event_generic
            )
            2 -> StoryEvent(
                "CH2_START", "บทที่ 2: สมรภูมิเพลิงกัลป์",
                "แผ่นดินแยกออกเป็นเสี่ยงๆ ลาวาสีชาดไหลนองแทนที่ลำน้ำ ทหารนับพันยืนนิ่งแข็งเป็นหินในท่าทางที่กำลังต่อสู้",
                listOf(StoryOption("บุกทะลวงกองเพลิง", "ความร้อนแรงไม่อาจหยุดยั้งเจตจำนงของท่านได้...", false, null, { })),
                R.drawable.img_event_battlefield
            )
            3 -> StoryEvent(
                "CH3_START", "บทที่ 3: หุบเขาหมอกลวงตา",
                "หมอกหนาทึบปกคลุมทั่วทุกสารทิศ ท่านแทบมองไม่เห็นมือตัวเอง เสียงหัวเราะลึกลับดังก้องมาจากหน้าผา",
                listOf(StoryOption("ไต่ขึ้นสู่ยอดเขา", "ทุกย่างก้าวเต็มไปด้วยอันตราย...", false, null, { })),
                R.drawable.img_event_generic
            )
            else -> getRandomEvent()
        }
    }

    // --- 2. บทส่งท้าย (Chapter End/Epilogue) ---
    fun getChapterEnd(chapter: Int): StoryEvent {
        return when (chapter) {
            1 -> StoryEvent(
                "CH1_END", "บทส่งท้าย: แสงรำไรในธุลี",
                "หมู่บ้านได้รับการปลดปล่อยแล้ว แต่ความโกลาหลยังไม่สิ้นสุด... ท่านเห็นเงาของทัพใหญ่ที่กำลังมุ่งหน้าไปทางทิศตะวันออก",
                listOf(StoryOption("เดินทางตามไป", "ร่องรอยของสงครามชัดเจนขึ้น...", false, null, { GameSession.isMainStoryEvent = true })),
                R.drawable.img_event_military
            )
            2 -> StoryEvent(
                "CH2_END", "บทส่งท้าย: เพลิงที่มอดดับ",
                "สมรภูมิเริ่มเย็นลง แต่หัวใจของเหล่าทหารยังคงลุกโชนด้วยความแค้น... เส้นทางเบื้องหน้าคือขุนเขาที่สูงเสียดฟ้า",
                listOf(StoryOption("มุ่งสู่ยอดเขา", "อากาศเริ่มหนาวเย็นลงอย่างรวดเร็ว...", false, null, { GameSession.isMainStoryEvent = true })),
                R.drawable.img_event_battlefield
            )
            else -> StoryEvent(
                "EPILOGUE", "ตำนานบทใหม่",
                "ชัยชนะครั้งนี้เป็นเพียงจุดเริ่มต้นของเรื่องราวที่ยิ่งใหญ่กว่า...",
                listOf(StoryOption("ก้าวต่อไป", "มุ่งหน้าสู่โชคชะตา...", false, null, { GameSession.isMainStoryEvent = true })),
                R.drawable.img_event_generic
            )
        }
    }

    // --- 3. ทางเลือกหลังปราบบอส (Boss Choice) ---
    fun getBossPostChoice(bossId: String): StoryEvent {
        return when (bossId) {
            "CORRUPTED_GENERAL" -> StoryEvent(
                "CHOICE_CH1", "ชะตากรรมของขุนพลผู้วิบัต",
                "แม่ทัพศัตรูคุกเข่าลงต่อหน้าท่าน 'ข้า... เพียงแค่อยากปกป้อง... บ้านของข้า...'",
                listOf(
                    StoryOption("ไว้ชีวิต (Spare)", "ท่านได้รับรู้เรื่องราวที่น่าเศร้า (+Honor)", false, null, { 
                        GameSession.spareCount++; GameSession.honorLevel++; GameSession.chaosLevel--
                    }, isSpare = true),
                    StoryOption("สังหาร (Execute)", "ท่านดับลมหายใจเขาเพื่อชิงพลังมา (+Chaos, +Strength)", false, null, { 
                        GameSession.executeCount++; GameSession.chaosLevel++; GameSession.player.strength += 1
                    }, isExecute = true)
                ),
                R.drawable.img_event_military
            )
            else -> StoryEvent(
                "VICTORY", "ชัยชนะครั้งยิ่งใหญ่",
                "ศัตรูที่แข็งแกร่งพ่ายแพ้ลงแล้ว เส้นทางเบื้องหน้าเปิดออกให้ท่านตัดสินใจ",
                listOf(
                    StoryOption("เดินทัพต่อ", "มุ่งหน้าสู่เป้าหมายถัดไป", false, null, { }, isSpare = true)
                ),
                R.drawable.img_event_military
            )
        }
    }

    // --- 4. เหตุการณ์สุ่ม (Random Events) ---
    fun getAbandonedShrine() = StoryEvent("SHRINE", "ศาลเจ้าที่ถูกทิ้งร้าง", "ศาลเจ้าเก่าแก่ตั้งอยู่อย่างเงียบสงบ ไม่ถูกแตะต้องโดยไฟสงคราม", listOf(
        StoryOption("สวดภาวนา", "ท่านได้รับความสงบทางจิตใจ (+Honor, ฟื้นฟู)", false, null, { GameSession.honorLevel++; GameSession.hiddenFlags["shrine_prayed"] = true; it.hp = minOf(it.maxHp, it.hp + 2) }),
        StoryOption("เมินเฉย", "ท่านมุ่งหน้าเดินต่อโดยไม่หยุดพัก", false, null, { }),
        StoryOption("ลบหลู่ศาลเจ้า", "ท่านรื้อค้นของมีค่ามาเป็นของตน (+Gold, +Chaos)", false, null, { GameSession.chaosLevel++; it.gold += 40 })
    ), R.drawable.img_event_generic)

    fun getWoundedSoldier() = StoryEvent("SOLDIER", "ทหารที่บาดเจ็บ", "ท่านพบทหารนายหนึ่งนอนบาดเจ็บสาหัส หายใจรวยรินอยู่ข้างทาง", listOf(
        StoryOption("ช่วยเหลือเขา", "ท่านสละยาเพื่อช่วยชีวิตเขา (-HP, +Honor)", false, null, { it.hp -= 2; GameSession.honorLevel++; GameSession.hiddenFlags["soldier_helped"] = true }),
        StoryOption("จบความทรมาน", "ท่านจบชีวิตเขาและหยิบทองมา (+Gold, +Chaos)", false, null, { it.gold += 20; GameSession.chaosLevel++ }),
        StoryOption("เดินจากไป", "ท่านเลือกที่จะไม่ยุ่งเกี่ยว", false, null, { })
    ), R.drawable.img_event_generic)

    fun getStrangeMerchant() = StoryEvent("MERCHANT", "พ่อค้าพเนจรลึกลับ", "พ่อค้าคนหนึ่งกระซิบกับท่าน: 'ข้ามีสิ่งที่ท่านต้องการ... ในราคาที่ท่านอาจไม่เชื่อ'", listOf(
        StoryOption("ซื้อของต้องสาป", "ท่านได้รับพลังแต่ต้องแลกด้วยวิญญาณ (+Strength, +Chaos)", false, null, { it.strength += 2; GameSession.chaosLevel += 2; GameSession.hiddenFlags["merchant_cursed"] = true }),
        StoryOption("ปฏิเสธ", "ท่านเดินหนีออกมาอย่างระมัดระวัง", false, null, { }),
        StoryOption("ข่มขู่ชิงทอง", "ท่านใช้กำลังบีบบังคับพ่อค้า (+Gold หรือ +Chaos)", false, null, { if (Random.nextBoolean()) it.gold += 50 else GameSession.chaosLevel++ })
    ), R.drawable.img_event_generic)

    fun getMilitaryEncounter(): StoryEvent {
        return when (Random.nextInt(5)) {
            0 -> StoryEvent("MIL_1", "หน่วยลาดตระเวนซุ่มโจมตี", "ทหารลาดตระเวนล้อมท่านไว้ในมุมอับ!", listOf(
                StoryOption("สู้หน้าตรง", "เข้าสู่โหมดต่อสู้ปกติ", true, Enemy("PATROL", "หน่วยลาดตระเวน", 15, 15, 2), { }),
                StoryOption("ชิงโจมตีก่อน", "พุ่งเข้าหาศัตรูทันที (+Bonus Damage)", true, Enemy("PATROL", "หน่วยลาดตระเวน", 15, 15, 2), { GameSession.firstAttackBonusPerTurn = 0.5 }, firstTurnBonus = true)
            ), R.drawable.img_event_military)
            1 -> StoryEvent("MIL_2", "กองกำลังยอดฝีมือ", "องครักษ์เกราะหนักยืนขวางทางผ่านหุบเขา", listOf(
                StoryOption("ท้าทายศัตรู", "การประลองกำลังเริ่มต้นขึ้น", true, Enemy("ELITE", "กองกำลังยอดฝีมือ", 40, 40, 4), { })
            ), R.drawable.img_event_elite)
            else -> getAmbushedPatrol()
        }
    }

    fun getAmbushedPatrol() = StoryEvent("MIL_P", "ทหารลาดตระเวน", "ท่านเผชิญหน้ากับหน่วยลาดตระเวนของศัตรู", listOf(
        StoryOption("ต่อสู้", "เริ่มการปะทะ", true, Enemy("PATROL", "ทหารลาดตระเวน", 20, 20, 2), { })
    ), R.drawable.img_event_military)

    fun getShopStoryEvent(): StoryEvent {
        val merchant = ShopLibrary.getMerchantEvent()
        return StoryEvent(merchant.id, "ร้านค้าลึกลับ", merchant.entryDialogue, merchant.options.map { opt ->
            StoryOption("${opt.text} (${opt.rewardPreview})", opt.dialogue, false, null, { player ->
                if (player.gold >= opt.costGold && player.hp > opt.costHp) {
                    player.gold -= opt.costGold
                    player.hp -= opt.costHp
                    opt.onSelect(player)
                }
            })
        }, R.drawable.img_event_generic)
    }

    fun getRandomEvent(): StoryEvent {
        val dice = Random.nextInt(100)
        return when {
            dice < 60 -> getMilitaryEncounter()
            else -> getPeacefulRandomEvent()
        }
    }

    fun getPeacefulRandomEvent(): StoryEvent {
        return when (Random.nextInt(3)) {
            0 -> getAbandonedShrine()
            1 -> getWoundedSoldier()
            else -> getStrangeMerchant()
        }
    }
}
