package com.rogie.threekingdoms.model

import com.rogie.threekingdoms.game.GameSession
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
    val options: List<StoryOption>
)

object StoryLibrary {

    fun getMilitaryEncounter(): StoryEvent {
        val encounters = listOf(
            StoryEvent(
                "AMBUSHED_PATROL",
                "ทหารลาดตระเวนซุ่มโจมตี",
                "กลุ่มทหารลาดตระเวน 3 นายล้อมท่านไว้ในมุมอับ ท่านจะรับมืออย่างไร?",
                listOf(
                    StoryOption("สู้หน้าตรง", "ท่านตั้งดาบเตรียมรับศึกที่ยุติธรรม", true, Enemy("PATROL", "หน่วยลาดตระเวน (3 นาย)", 12, 12, 1, speed = 10), { }),
                    StoryOption("ชิงโจมตีก่อน", "ท่านพุ่งเข้าหาพวกมันก่อนที่จะทันตั้งตัว!", true, Enemy("PATROL", "หน่วยลาดตระเวน (3 นาย)", 12, 12, 1, speed = 15), { }, firstTurnBonus = true),
                    StoryOption("ตั้งรับอย่างเหนียวแน่น", "ท่านใช้พื้นที่ให้เป็นประโยชน์เพื่อป้องกัน", true, Enemy("PATROL", "หน่วยลาดตระเวน (3 นาย)", 12, 12, 1, speed = 8), { it.block += 2 })
                )
            ),
            StoryEvent(
                "WAR_CAMP",
                "บุกค่ายทหารศัตรู",
                "ท่านพบยอดค่ายทหารที่มีทหารเฝ้าอยู่หนาแน่น มีทั้งทหารเลวและนายกอง",
                listOf(
                    StoryOption("บุกโจมตีทันที", "ท่านบุกเข้ากลางค่ายด้วยความบ้าคลั่ง!", true, Enemy("WAR_CAMP", "กองกำลังค่ายทหาร (6 นาย)", 18, 18, 1, isBoss = true), { }),
                    StoryOption("ลอบทำลายเสบียง", "ไฟลามไปทั่วค่าย ทหารศัตรูเริ่มอ่อนแรง", true, Enemy("WAR_CAMP", "กองกำลังค่ายทหาร (6 นาย)", 12, 12, 1), { }),
                    StoryOption("ซุ่มรอโอกาส", "ท่านรอจนทหารบางส่วนออกไปลาดตระเวน", true, Enemy("WAR_CAMP", "กองกำลังค่ายทหาร (3 นาย)", 10, 10, 1), { })
                )
            )
        )
        return encounters.random()
    }

    fun getPeacefulEncounter(): StoryEvent {
        val encounters = listOf(
            StoryEvent(
                "ABANDONED_SHRINE",
                "ศาลเจ้าที่ถูกทิ้งร้าง",
                "ศาลเจ้าเก่าแก่ตั้งอยู่อย่างเงียบสงบ ไม่ถูกแตะต้องโดยสงคราม",
                listOf(
                    StoryOption("สวดภาวนา", "ท่านได้รับความสงบทางจิตใจและรักษาแผลบางส่วน", false, null, { 
                        GameSession.honorLevel++
                        it.hp = minOf(it.maxHp, it.hp + 1)
                    }),
                    StoryOption("เมินเฉย", "ท่านมุ่งหน้าเดินต่อโดยไม่หยุดพัก", false, null, { }),
                    StoryOption("ลบหลู่ศาลเจ้า", "ท่านรื้อค้นของมีค่าในศาลเจ้ามาเป็นของตน", false, null, { 
                        GameSession.chaosLevel++
                        it.gold += 30
                    })
                )
            ),
            StoryEvent(
                "WOUNDED_SOLDIER_STORY",
                "ทหารที่บาดเจ็บ",
                "ท่านพบทหารนายหนึ่งนอนบาดเจ็บสาหัส หายใจรวยรินอยู่ข้างทาง",
                listOf(
                    StoryOption("ช่วยเหลือเขา", "ท่านสละยาและเวลาเพื่อช่วยชีวิตเขา", false, null, { 
                        it.hp -= 1
                        GameSession.honorLevel++
                    }),
                    StoryOption("ปลิดชีพเพื่อชิงทรัพย์", "ท่านจบความทรมานของเขาและเอาทองมา", false, null, { 
                        it.gold += 15
                        GameSession.chaosLevel++
                    }),
                    StoryOption("ปล่อยไว้แบบนั้น", "ท่านเดินผ่านเขาไปอย่างไร้เยื่อใย", false, null, { })
                )
            ),
            StoryEvent(
                "STRANGE_MERCHANT",
                "พ่อค้าประหลาด",
                "พ่อค้าคนหนึ่งกระซิบกับท่าน: 'ข้าขายในสิ่งที่คนอื่นหวาดกลัว...'",
                listOf(
                    StoryOption("ซื้อไอเทมต้องสาป (20 ทอง)", "ท่านได้รับพลังมหาศาลแต่ต้องแลกมาด้วยความโกลาหล", false, null, { 
                        it.gold -= 20
                        it.strength += 2
                        GameSession.chaosLevel += 2
                    }),
                    StoryOption("ปฏิเสธ", "ท่านเดินหนีออกมาอย่างระมัดระวัง", false, null, { }),
                    StoryOption("ข่มขู่ชิงทอง", "ท่านใช้กำลังบีบบังคับพ่อค้า!", false, null, { 
                        it.gold += 30
                        GameSession.chaosLevel += 1
                    })
                )
            )
        )
        return encounters.random()
    }

    fun getShopStoryEvent(): StoryEvent {
        val merchant = ShopLibrary.getMerchantEvent()
        return StoryEvent(
            merchant.id,
            merchant.name,
            merchant.entryDialogue,
            merchant.options.map { opt ->
                StoryOption(
                    "${opt.text} (${opt.rewardPreview})",
                    opt.dialogue,
                    false,
                    null,
                    { player ->
                        if (player.gold >= opt.costGold && player.hp > opt.costHp) {
                            player.gold -= opt.costGold
                            player.hp -= opt.costHp
                            opt.onSelect(player)
                        }
                    }
                )
            }
        )
    }

    fun getChapterStart(chapter: Int): StoryEvent {
        return when (chapter) {
            1 -> StoryEvent(
                "CH1_START",
                "บทที่ 1: หมู่บ้านแห่งธุลี",
                "หมู่บ้านที่เคยสงบสุขบัดนี้เหลือเพียงซากปรักหักพัง เสียงนกร้องถูกแทนที่ด้วยเสียงกรีดร้องของวิญญาณที่วนเวียนอยู่ในหมอกหนา ท่านจะทำอย่างไร?",
                listOf(
                    StoryOption("สำรวจซากปรักหักพัง", "ท่านพบเศษจดหมายของเด็กสาวที่พรรณนาถึงพ่อที่ไม่ได้กลับบ้าน...", false, null, { }),
                    StoryOption("ไล่ตามเงาปริศนา", "ท่านไล่ตามเงาทมิฬที่วูบไหวผ่านบ้านที่ไฟกำลังมอดไหม้", false, null, { })
                )
            )
            2 -> StoryEvent(
                "CH2_START",
                "บทที่ 2: สมรภูมิเพลิงกัลป์",
                "แผ่นดินแยกออกเป็นเสี่ยงๆ ลาวาสีชาดไหลนองแทนที่ลำน้ำ ทหารนับพันยืนนิ่งแข็งเป็นหินในท่าทางที่กำลังต่อสู้",
                listOf(
                    StoryOption("ช่วยชีวิตเหล่าทหาร", "ท่านพยายามดึงทหารออกจากกองเพลิง...", false, null, { }),
                    StoryOption("มุ่งหน้าต่อไปเพียงลำพัง", "ท่านก้าวข้ามร่างทหารที่กำลังมอดไหม้...", false, null, { })
                )
            )
            else -> getRandomEvent()
        }
    }

    fun getBossPostChoice(bossId: String): StoryEvent {
        return when (bossId) {
            "CORRUPTED_GENERAL" -> StoryEvent(
                "CHOICE_CH1",
                "ชะตากรรมของแม่ทัพผู้แปดเปื้อน",
                "แม่ทัพศัตรูคุกเข่าลงต่อหน้าท่าน 'ข้า... เพียงแค่อยากปกป้อง... บ้านของข้า...'",
                listOf(
                    StoryOption("ไว้ชีวิต (Spare)", "ท่านได้รับรู้เรื่องราวของ 'สงครามแห่งโกลาหล'", false, null, { 
                        GameSession.spareCount++
                        GameSession.chaosLevel--
                        GameSession.honorLevel++
                    }, isSpare = true),
                    StoryOption("สังหาร (Execute)", "ท่านสังหารเขาเพื่อชิงพลังมาเป็นของตนเอง", false, null, { 
                        GameSession.executeCount++
                        GameSession.chaosLevel++
                        GameSession.player.strength += 1
                    }, isExecute = true)
                )
            )
            else -> StoryEvent("NONE", "", "", emptyList())
        }
    }

    fun getRandomEvent(): StoryEvent {
        val dice = Random.nextInt(100)
        return when {
            dice < 15 -> getMilitaryEncounter() // Reduced chance from 30 to 15
            dice < 40 -> getShopStoryEvent()   // Added Shop chance
            else -> getPeacefulEncounter()      // Increased Peaceful events
        }
    }
}
