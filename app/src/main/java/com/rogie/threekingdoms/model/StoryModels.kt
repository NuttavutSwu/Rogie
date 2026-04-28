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

    // --- CHAPTER FLOW ---
    fun getChapterStart(chapter: Int): StoryEvent = when (chapter) {
        1 -> StoryEvent("CH1_START", "บทที่ 1: หมู่บ้านแห่งธุลี", "หมู่บ้านในซากปรักหักพัง...", listOf(StoryOption("สำรวจ", "เริ่มเดินทาง...", false, null, {})), R.drawable.village)
        2 -> StoryEvent("CH2_START", "บทที่ 2: สมรภูมิเพลิงกัลป์", "แผ่นดินลุกเป็นไฟ...", listOf(StoryOption("บุกทะลวง", "ลุยไฟ...", false, null, {})), R.drawable.flamevillage)
        else -> getRandomEvent()
    }

    fun getChapterEnd(chapter: Int): StoryEvent = StoryEvent("CH_END", "บทส่งท้ายที่ $chapter", "การเดินทางยังไม่จบลง...", listOf(StoryOption("ไปต่อ", "Onward!", false, null, { GameSession.isMainStoryEvent = true })), R.drawable.img_event_military)

    fun getBossPostChoice(bossId: String): StoryEvent = StoryEvent("BOSS_CHOICE", "ชะตากรรมผู้พ่ายแพ้", "ศัตรูคุกเข่าลง...", listOf(
        StoryOption("ไว้ชีวิต", "Honor เพิ่มขึ้น", false, null, { GameSession.spareCount++; GameSession.honorLevel++ }, isSpare = true),
        StoryOption("สังหาร", "Chaos เพิ่มขึ้น", false, null, { GameSession.executeCount++; GameSession.chaosLevel++ }, isExecute = true)
    ), R.drawable.img_event_military)

    // --- MILITARY ENCOUNTERS ---
    fun milPatrol() = StoryEvent("MIL_P", "หน่วยลาดตระเวนซุ่มโจมตี", "ทหารล้อมท่านไว้!", listOf(StoryOption("สู้", "โจมตี!", true, Enemy("P", "ลาดตระเวน", 15, 15, 2), {})), R.drawable.img_event_military)
    fun milElite() = StoryEvent("MIL_E", "องครักษ์เกราะหนัก", "ยอดฝีมือขวางทางอยู่", listOf(StoryOption("ท้าดวล", "เริ่มสู้", true, Enemy("E", "องครักษ์", 40, 40, 4), {})), R.drawable.img_event_elite)
    fun milNight() = StoryEvent("MIL_N", "ลอบโจมตียามวิกาล", "ศัตรูในเงามืด", listOf(StoryOption("จุดไฟ", "มองเห็นศัตรู", true, Enemy("N", "นักฆ่า", 20, 20, 3), {})), R.drawable.img_event_military)
    fun milCamp() = StoryEvent("MIL_C", "บุกค่ายทหาร", "ท่านพบค่ายศัตรู", listOf(StoryOption("บุกทันที", "โจมตีเร็ว!", true, Enemy("C", "ค่ายทหาร", 50, 50, 4), { GameSession.hiddenFlags["camp_attack"] = true })), R.drawable.img_event_military)
    fun milDuel() = StoryEvent("MIL_D", "ยอดขุนพลท้าดวล", "เขาท้าทายเกียรติยศท่าน", listOf(StoryOption("รับคำท้า", "+Honor", true, Enemy("D", "ขุนพล", 35, 35, 4), { GameSession.honorLevel++ })), R.drawable.img_event_duel)
    fun milBeast() = StoryEvent("MIL_B", "หน่วยสัตว์สงคราม", "อสูรสงครามกำลังคำราม", listOf(StoryOption("เล็งที่สัตว์", "ฆ่าสัตว์ก่อน", true, Enemy("B", "สัตว์ร้าย", 50, 50, 5), { GameSession.hiddenFlags["kill_beast"] = true })), R.drawable.img_event_beast)
    fun milSiege() = StoryEvent("MIL_S", "หอคอยล้อมเมืองลุกไหม้", "ศัตรูโรยตัวลงมาจากหอคอย", listOf(StoryOption("พุ่งชน", "ลุย!", true, Enemy("S", "ทหารเพลิง", 30, 30, 4), {})), R.drawable.img_event_siege)
    fun milArchers() = StoryEvent("MIL_A", "ฝนธนูจากฟากฟ้า", "ธนูนับร้อยกำลังพุ่งมา", listOf(StoryOption("ยกโล่", "+Block", true, Enemy("A", "ทหารธนู", 20, 20, 2), { GameSession.extraStartBlock += 10 })), R.drawable.img_event_archers)
    fun milCavalry() = StoryEvent("MIL_V", "กองทหารม้าจู่โจม", "ม้านับร้อยวิ่งหวดมา", listOf(StoryOption("ตั้งรับ", "ลดดาเมจ", true, Enemy("V", "ทหารม้า", 45, 45, 5), { GameSession.damageReduction = 0.5 })), R.drawable.img_event_cavalry)
    fun milExecutioner() = StoryEvent("MIL_X", "เพชฌฆาตสนามรบ", "นักล่าที่กำลังตามหาเหยื่อ", listOf(StoryOption("ประลอง", "พิสูจน์ฝีมือ", true, Enemy("X", "เพชฌฆาต", 60, 60, 6), { GameSession.hiddenFlags["duel_x"] = true })), R.drawable.img_event_executioner)

    // --- PEACEFUL ENCOUNTERS ---
    fun pShrine() = StoryEvent("P_S", "ศาลเจ้าลึกลับ", "ที่พึ่งสุดท้ายของผู้หลงทาง", listOf(StoryOption("ขอพร", "ฟื้นฟู", false, null, { it.hp = minOf(it.maxHp, it.hp + 2); GameSession.honorLevel++ })), R.drawable.img_event_generic)
    fun pMerchant() = StoryEvent("P_M", "พ่อค้าพเนจร", "เขามีสินค้าแปลกตา", listOf(StoryOption("ซื้อของ", "-Gold +Power", false, null, { it.gold -= 30; it.strength += 1 })), R.drawable.img_event_generic)
    fun pSoldier() = StoryEvent("P_L", "ทหารหลงทัพ", "เขาร้องขอความช่วยเหลือ", listOf(StoryOption("ช่วย", "-HP +Honor", false, null, { it.hp -= 1; GameSession.honorLevel++ })), R.drawable.img_event_generic)
    fun pLibrary() = StoryEvent("P_B", "หอจดหมายเหตุที่ถูกลืม", "ความรู้โบราณถูกเก็บที่นี่", listOf(StoryOption("ศึกษา", "+Energy", false, null, { it.baseEnergy += 1 })), R.drawable.img_event_generic)

    fun getRandomEvent(): StoryEvent {
        val dice = Random.nextInt(100)
        return when {
            dice < 70 -> getMilitaryEncounter() // 70% Combat
            else -> getPeacefulRandomEvent()    // 30% Non-combat
        }
    }

    fun getMilitaryEncounter(): StoryEvent = when (Random.nextInt(10)) {
        0 -> milPatrol()
        1 -> milElite()
        2 -> milNight()
        3 -> milCamp()
        4 -> milDuel()
        5 -> milBeast()
        6 -> milSiege()
        7 -> milArchers()
        8 -> milCavalry()
        else -> milExecutioner()
    }

    fun getPeacefulRandomEvent(): StoryEvent = when (Random.nextInt(4)) {
        0 -> pShrine()
        1 -> pMerchant()
        2 -> pSoldier()
        else -> pLibrary()
    }

    fun getShopStoryEvent(): StoryEvent {
        val merchant = ShopLibrary.getMerchantEvent()
        return StoryEvent(merchant.id, "ร้านค้าพเนจร", merchant.entryDialogue, merchant.options.map { opt ->
            StoryOption("${opt.text} (${opt.rewardPreview})", opt.dialogue, false, null, { player ->
                if (player.gold >= opt.costGold && player.hp > opt.costHp) {
                    player.gold -= opt.costGold; player.hp -= opt.costHp; opt.onSelect(player)
                }
            })
        }, R.drawable.img_event_generic)
    }
}
