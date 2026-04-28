package com.rogie.threekingdoms.model

import com.rogie.threekingdoms.game.GameSession

enum class ShopOptionType {
    BUY_CARD, BUY_RELIC, TRADE_HEALTH, REMOVE_CARD, GAMBLE, LEAVE, MYSTERIOUS_BOX
}

data class ShopOption(
    val type: ShopOptionType,
    val text: String,
    val dialogue: String,
    val costGold: Int = 0,
    val costHp: Int = 0,
    val rewardPreview: String = "",
    val onSelect: (Player) -> Unit
)

data class MerchantEvent(
    val id: String,
    val name: String,
    val entryDialogue: String,
    val isSuspicious: Boolean = false,
    val options: List<ShopOption>
)

object ShopLibrary {
    fun getMerchantEvent(): MerchantEvent {
        val isSuspicious = kotlin.random.Random.nextInt(100) < 20
        val priceMult = if (isSuspicious) 0.7 else 1.0
        
        val options = mutableListOf<ShopOption>()
        
        options.add(ShopOption(
            ShopOptionType.BUY_CARD,
            "ซื้อการ์ดใหม่ (${(50 * priceMult).toInt()} ทอง)",
            "เลือกให้ดี พลังมักมาพร้อมกับราคาเสมอ...",
            costGold = (50 * priceMult).toInt(),
            rewardPreview = "สุ่มการ์ด 1 ใน 3 ใบ",
            onSelect = { /* Handled in Activity UI */ }
        ))

        options.add(ShopOption(
            ShopOptionType.BUY_RELIC,
            "ซื้อสมบัติโบราณ (${(100 * priceMult).toInt()} ทอง)",
            "ชิ้นนี้... ค่อนข้างพิเศษทีเดียว",
            costGold = (100 * priceMult).toInt(),
            rewardPreview = "สุ่มรับ Relic 1 ชิ้น",
            onSelect = { /* Handled in Activity UI */ }
        ))

        options.add(ShopOption(
            ShopOptionType.TRADE_HEALTH,
            "แลกเปลี่ยนวิญญาณ (เสีย 1 HP)",
            "ข้อเสนอที่ยุติธรรม... ชีวิตของท่านเพื่อพลังอำนาจ",
            costHp = 1,
            rewardPreview = "สุ่มรับการ์ด Rare หรือ Relic",
            onSelect = { /* Handled in Activity UI */ }
        ))

        options.add(ShopOption(
            ShopOptionType.REMOVE_CARD,
            "ทำลายพันธนาการ (${(30 * priceMult).toInt()} ทอง)",
            "บางครั้ง... การมีน้อยกว่าก็คือการมีมากกว่า",
            costGold = (30 * priceMult).toInt(),
            rewardPreview = "ลบการ์ด 1 ใบออกจากสำรับ",
            onSelect = { /* Handled in Activity UI */ }
        ))

        if (isSuspicious) {
            options.add(ShopOption(
                ShopOptionType.MYSTERIOUS_BOX,
                "รับกล่องปริศนา (ฟรี)",
                "เชื่อใจข้า... หรือจะไม่เชื่อก็ได้",
                rewardPreview = "รางวัลใหญ่ หรือ คำสาป",
                onSelect = { /* Handled in Activity UI */ }
            ))
        } else {
            options.add(ShopOption(
                ShopOptionType.GAMBLE,
                "เสี่ยงโชค (${(20 * priceMult).toInt()} ทอง)",
                "โชคชะตาเข้าข้างผู้ที่กล้าหาญ... หรือคนเขลา",
                costGold = (20 * priceMult).toInt(),
                rewardPreview = "50% รับทองเพิ่ม 2 เท่า / 50% เสีย HP",
                onSelect = { /* Handled in Activity UI */ }
            ))
        }

        options.add(ShopOption(
            ShopOptionType.LEAVE,
            "จากไป",
            "ไว้โอกาสหน้าก็แล้วกัน",
            onSelect = { }
        ))

        return MerchantEvent(
            if (isSuspicious) "SUSPICIOUS_MERCHANT" else "WANDERING_MERCHANT",
            if (isSuspicious) "พ่อค้าผู้น่าสงสัย" else "พ่อค้าพเนจร",
            if (isSuspicious) "พ่อค้าในเงามืดส่งยิ้มให้ท่านอย่างมีเลศนัย..." else "พ่อค้าคนหนึ่งยืนอยู่ท่ามกลางสมรภูมิ ยิ้มอย่างสงบ",
            isSuspicious,
            options
        )
    }
}
