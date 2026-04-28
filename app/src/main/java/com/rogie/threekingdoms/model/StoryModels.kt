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
        1 -> StoryEvent("CH1_START", "บทที่ 1: หมู่บ้านแห่งธุลี", "หมู่บ้านในซากปรักหักพังที่เต็มไปด้วยกลิ่นอายแห่งความสิ้นหวัง...", listOf(StoryOption("สำรวจหาเบาะแส", "เริ่มเดินทาง...", false, null, {})), R.drawable.village)
        2 -> StoryEvent("CH2_START", "บทที่ 2: สมรภูมิเพลิงกัลป์", "แผ่นดินลุกเป็นไฟจากเพลิงสงครามที่ไม่มีวันมอดดับ...", listOf(StoryOption("บุกทะลวงฝ่าเปลวเพลิง", "ลุยไฟ...", false, null, {})), R.drawable.flamevillage)
        else -> getRandomEvent()
    }

    fun getChapterEnd(chapter: Int): StoryEvent = StoryEvent("CH_END", "บทส่งท้ายที่ $chapter", "การเดินทางในสมรภูมินี้สิ้นสุดลง แต่สงครามยังคงดำเนินต่อไป...", listOf(StoryOption("มุ่งหน้าสู่สมรภูมิถัดไป", "Onward!", false, null, { GameSession.isMainStoryEvent = true })), R.drawable.img_event_military)

    fun getBossPostChoice(bossId: String): StoryEvent = StoryEvent("BOSS_CHOICE", "ชะตากรรมผู้พ่ายแพ้", "ศัตรูคุกเข่าลงต่อหน้าท่าน ชะตาของเขาอยู่ในกำมือท่านแล้ว...", listOf(
        StoryOption("ไว้ชีวิต (Mercy)", "ได้รับความเคารพ (Honor++)", false, null, { GameSession.spareCount++; GameSession.honorLevel++ }, isSpare = true),
        StoryOption("สังหาร (Execution)", "ปลุกความกระหายเลือด (Chaos++)", false, null, { GameSession.executeCount++; GameSession.chaosLevel++ }, isExecute = true)
    ), R.drawable.img_event_military)

    // --- MILITARY ENCOUNTERS (Multiple Options) ---
    
    fun milPatrol() = StoryEvent("MIL_P", "หน่วยลาดตระเวนซุ่มโจมตี", "ทหารล้อมท่านไว้ในตรอกแคบ!", listOf(
        StoryOption("ชักดาบสู้", "โจมตีสายฟ้าแลบ!", true, Enemy("P", "ลาดตระเวน", 12, 12, 2), {}),
        StoryOption("ติดสินบน (15 Gold)", "ทหารรับเงินและถอยไปอย่างเงียบเชียบ", false, null, { it.gold -= 15 }),
        StoryOption("ข่มขู่", "ใช้นามของท่านขู่ให้ถอย (ต้องมี Honor 2)", false, null, { 
            if (GameSession.honorLevel >= 2) { /* Success */ } else { it.hp -= 1 } 
        })
    ), R.drawable.img_event_military)

    fun milElite() = StoryEvent("MIL_E", "องครักษ์เกราะหนัก", "ยอดฝีมือขวางทางสำคัญอยู่ เขาดูแข็งแกร่งเกินกว่าจะประมาท", listOf(
        StoryOption("ท้าดวลอย่างสมเกียรติ", "เริ่มการประลองที่ยุติธรรม", true, Enemy("E", "องครักษ์", 30, 30, 4), { GameSession.honorLevel++ }),
        StoryOption("ลอบโจมตีจากเงามืด", "สร้างความเสียหายก่อนเริ่ม (Chaos+)", true, Enemy("E", "องครักษ์", 20, 30, 4), { GameSession.chaosLevel++ })
    ), R.drawable.img_event_elite)

    fun milSupplyTrain() = StoryEvent("MIL_TR", "ขบวนเสบียงศัตรู", "ขบวนเสบียงขนาดใหญ่กำลังเคลื่อนผ่านโดยมีการป้องกันที่เบาบาง...", listOf(
        StoryOption("ปล้นเสบียง", "ได้ทองจำนวนมากแต่เพิ่มความโกลาหล", true, Enemy("TR", "กองเสบียง", 35, 35, 3), { it.gold += 50; GameSession.chaosLevel++ }),
        StoryOption("ปล่อยไป", "รักษาคุณธรรมของนักรบ", false, null, { GameSession.honorLevel++ })
    ), R.drawable.img_event_military)

    fun milBridge() = StoryEvent("MIL_BR", "ด่านตรวจบนสะพาน", "ทหารเรียกเก็บค่าผ่านทางมหาศาลเพื่อข้ามแม่น้ำสายนี้", listOf(
        StoryOption("จ่ายค่าผ่านทาง (25 Gold)", "ข้ามไปอย่างสะดวกสบาย", false, null, { it.gold -= 25 }),
        StoryOption("ฝ่าด่านตรวจ", "ไม่มีใครขวางทางข้าได้!", true, Enemy("BR", "ทหารด่าน", 25, 25, 3), {}),
        StoryOption("หาทางลับใต้สะพาน", "เสี่ยงอันตราย (เสีย 1 HP)", false, null, { it.hp -= 1 })
    ), R.drawable.img_event_battlefield)

    fun milDuel() = StoryEvent("MIL_D", "ยอดขุนพลท้าดวล", "เขาท้าทายเกียรติยศท่านต่อหน้าเหล่าทหารหาญ", listOf(
        StoryOption("รับคำท้า", "สู้เพื่อศักดิ์ศรี (+Honor)", true, Enemy("D", "ขุนพล", 25, 25, 4), { GameSession.honorLevel++ }),
        StoryOption("ปฏิเสธอย่างเย็นชา", "หลีกเลี่ยงความเสี่ยงที่ไม่จำเป็น", false, null, { GameSession.honorLevel-- })
    ), R.drawable.img_event_duel)

    fun milExecutioner() = StoryEvent("MIL_X", "เพชฌฆาตสนามรบ", "นักล่าที่กำลังตามหาเหยื่อรายสุดท้ายท่ามกลางซากศพ", listOf(
        StoryOption("หยุดยั้งความบ้าคลั่ง", "สู้เพื่อปกป้องผู้บริสุทธิ์", true, Enemy("X", "เพชฌฆาต", 45, 45, 6), { GameSession.honorLevel += 2 }),
        StoryOption("เดินจากไปอย่างเงียบๆ", "ไม่ใช่ธุระของข้า", false, null, { GameSession.chaosLevel++ })
    ), R.drawable.img_event_excutioner)

    // --- PEACEFUL ENCOUNTERS (Multiple Options) ---

    fun pShrine() = StoryEvent("P_S", "ศาลเจ้าลึกลับ", "ที่พึ่งสุดท้ายของผู้หลงทางท่ามกลางหมอกที่ปกคลุม", listOf(
        StoryOption("สวดมนต์ขอพร", "ฟื้นฟูพลังกาย (+2 HP)", false, null, { it.hp = minOf(it.maxHp, it.hp + 2); GameSession.honorLevel++ }),
        StoryOption("บริจาคทอง (30 Gold)", "ขอพรเพื่อโชคลาภในอนาคต (+Max HP)", false, null, { it.gold -= 30; it.maxHp += 1; it.hp += 1 }),
        StoryOption("หยิบทองจากถาดบูชา", "ได้ทอง (20 Gold) แต่ลบหลู่เทพเจ้า", false, null, { it.gold += 20; GameSession.chaosLevel += 2 })
    ), R.drawable.img_event_mist)

    fun pHermit() = StoryEvent("P_H", "กระท่อมฤๅษี", "นักปราชญ์ผู้สันโดษนั่งรอท่านอยู่เหมือนรู้ว่าท่านจะมา", listOf(
        StoryOption("ขอคำชี้แนะด้านกลยุทธ์", "เพิ่มปัญญาอันล้ำลึก (+Insight)", false, null, { it.insight += 1 }),
        StoryOption("ขอให้ช่วยรักษากาย", "ฟื้นฟูบาดแผลทั้งหมดที่ได้รับมา", false, null, { it.hp = it.maxHp })
    ), R.drawable.img_event_generic)

    fun pAncientAltar() = StoryEvent("P_A", "แท่นบูชาโบราณ", "มีพลังงานด้านมืดแผ่ออกมาอย่างรุนแรง...", listOf(
        StoryOption("สังเวยด้วยเลือด (1 HP)", "แลกกับพลังโจมตีถาวร (+1 Str)", false, null, { it.hp -= 1; it.strength += 1 }),
        StoryOption("สังเวยด้วยวิญญาณ (1 Max HP)", "แลกกับพลังงานที่ไร้ขีดจำกัด (+1 Energy)", false, null, { it.maxHp -= 1; if(it.hp > it.maxHp) it.hp = it.maxHp; it.baseEnergy += 1 }),
        StoryOption("ทำลายมันทิ้ง", "เพื่อไม่ให้ใครมาใช้พลังนี้อีก (+Honor)", false, null, { GameSession.honorLevel++ })
    ), R.drawable.img_event_generic)

    fun pMerchant() = StoryEvent("P_M", "พ่อค้าพเนจร", "เขามีสินค้าที่อาจช่วยให้ท่านรอดพ้นจากความตายได้", listOf(
        StoryOption("ซื้อเครื่องราง (30 Gold)", "เพิ่ม Strength", false, null, { it.gold -= 30; it.strength += 1 }),
        StoryOption("ซื้อยาอายุวัฒนะ (25 Gold)", "เพิ่ม Max HP", false, null, { it.gold -= 25; it.maxHp += 1; it.hp += 1 }),
        StoryOption("ขโมยของ", "เสี่ยงโชค (มีโอกาสได้ของฟรีหรือเสีย HP)", false, null, { 
            if(Random.nextBoolean()) it.strength += 1 else it.hp -= 2
            GameSession.chaosLevel++
        })
    ), R.drawable.img_event_generic)

    fun pLibrary() = StoryEvent("P_B", "หอจดหมายเหตุที่ถูกลืม", "ความรู้โบราณมากมายถูกเก็บรักษาไว้อย่างดีที่นี่", listOf(
        StoryOption("ศึกษาตำราพิชัยสงคราม", "เพิ่มขีดจำกัดพลังงานต่อเทิร์น (+1 Energy)", false, null, { it.baseEnergy += 1 }),
        StoryOption("ค้นหาแผนที่จุดยุทธศาสตร์", "ได้เปรียบในการป้องกัน (+15 Start Block)", false, null, { GameSession.extraStartBlock += 15 })
    ), R.drawable.img_event_generic)

    // --- RANDOM LOGIC ---

    fun getRandomEvent(): StoryEvent {
        val dice = Random.nextInt(100)
        return when {
            dice < 65 -> getMilitaryEncounter() // 65% Combat/Risk
            else -> getPeacefulRandomEvent()    // 35% Pure Benefit
        }
    }

    fun getMilitaryEncounter(): StoryEvent = when (Random.nextInt(6)) {
        0 -> milPatrol()
        1 -> milElite()
        2 -> milSupplyTrain()
        3 -> milBridge()
        4 -> milDuel()
        else -> milExecutioner()
    }

    fun getPeacefulRandomEvent(): StoryEvent = when (Random.nextInt(5)) {
        0 -> pShrine()
        1 -> pHermit()
        2 -> pAncientAltar()
        3 -> pMerchant()
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
