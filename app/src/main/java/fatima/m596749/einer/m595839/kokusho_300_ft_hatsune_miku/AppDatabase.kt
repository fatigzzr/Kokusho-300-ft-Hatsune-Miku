package fatima.m596749.einer.m595839.kokusho_300_ft_hatsune_miku

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

@Database(entities = [Character::class, CharacterReading::class, CharacterWord::class, Component::class, Radical::class, Song::class, SongCharacter::class], version = 2)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun kanjiDao() : KanjiDao
    abstract fun gameDao() : GameDao

    companion object {
        val characters = listOf(
            Character(1, "人", "person", false),
            Character(2, "男", "man", false),
            Character(3, "女", "woman", false),
            Character(4, "子", "child", false),
            Character(5, "車", "car", false),
            Character(6, "山", "mountain", false),
            Character(7, "川", "river", false),
            Character(8, "田", "rice field", false),
            Character(9, "米", "rice", false),
            Character(10, "雨", "rain", false),
            Character(11, "上", "above", false),
            Character(12, "中", "middle", false),
            Character(13, "下", "below", false),
            Character(14, "左", "left", false),
            Character(15, "右", "right", false),
            Character(16, "明", "bright", false),
            Character(17, "休", "rest", false),
            Character(18, "林", "woods", false),
            Character(19, "森", "forest", false),
            Character(20, "好", "like", false),
            Character(21, "一", "one", false),
            Character(22, "二", "two", false),
            Character(23, "三", "three", false),
            Character(24, "四", "four", false),
            Character(25, "五", "five", false),
            Character(26, "六", "six", false),
            Character(27, "七", "seven", false),
            Character(28, "ハ", "eight", false),
            Character(29, "九", "nine", false),
            Character(30, "十", "ten", false),
            Character(31, "百", "hundred", false),
            Character(32, "千", "thousand", false),
            Character(33, "万", "ten thousand", false),
            Character(34, "円", "yen", false),
            Character(35, "色", "color", false),
            Character(36, "白", "white", false),
            Character(37, "黒", "black", false),
            Character(38, "赤", "red", false),
            Character(39, "青", "blue", false),
            Character(40, "黄", "yellow", false),
            Character(41, "月", "moon", false),
            Character(42, "火", "fire", false),
            Character(43, "水", "water", false),
            Character(44, "木", "tree", false),
            Character(45, "金", "gold", false),
            Character(46, "土", "earth", false),
            Character(47, "日", "day", false),
            Character(48, "曜", "day of the week", false),
            Character(49, "毎", "every", false),
            Character(50, "週", "week", false),
            Character(51, "行", "go", false),
            Character(52, "来", "come", false),
            Character(53, "帰", "return", false),
            Character(54, "始", "begin", false),
            Character(55, "終", "end", false),
            Character(56, "起", "wake up", false),
            Character(57, "寝", "sleep", false),
            Character(58, "働", "work", false),
            Character(59, "勉", "endeavor", false),
            Character(60, "強", "strong", false),
            Character(61, "私", "me", false),
            Character(62, "家", "house", false),
            Character(63, "族", "family", false),
            Character(64, "父", "father", false),
            Character(65, "母", "mother", false),
            Character(66, "兄", "older brother", false),
            Character(67, "弟", "younger brother", false),
            Character(68, "姉", "older sister", false),
            Character(69, "妹", "younger sister", false),
            Character(70, "主", "master", false),
            Character(71, "内", "inside", false),
            Character(72, "奥", "interior", false),
            Character(73, "仕", "serve", false),
            Character(74, "事", "matter", false),
            Character(75, "生", "life", false),
            Character(76, "先", "previous", false),
            Character(77, "学", "study", false),
            Character(78, "会", "meet", false),
            Character(79, "社", "company", false),
            Character(80, "員", "member", false),
            Character(81, "時", "time", false),
            Character(82, "分", "minute", false),
            Character(83, "午", "noon", false),
            Character(84, "前", "before", false),
            Character(85, "後", "after", false),
            Character(86, "間", "interval", false),
            Character(87, "半", "half", false),
            Character(88, "朝", "morning", false),
            Character(89, "昼", "noon", false),
            Character(90, "晩", "evening", false),
            Character(91, "今", "now", false),
            Character(92, "去", "past", false),
            Character(93, "年", "year", false),
            Character(94, "夕", "evening", false),
            Character(95, "方", "direction", false),
            Character(96, "春", "spring", false),
            Character(97, "夏", "summer", false),
            Character(98, "秋", "autumn", false),
            Character(99, "冬", "winter", false),
            Character(100, "夜", "night", false)
        )

        val words = listOf(
            CharacterWord(1, "にほんじん", "日本人", "Japanese person"),
            CharacterWord(1, "あめりかじん", "アメリカ人", "American person"),
            CharacterWord(1, "～にん", "～人", "~ person"),
            CharacterWord(1, "にんぎょう", "人形", "doll"),
            CharacterWord(1, "ひと", "人", "person"),
            CharacterWord(1, "ひとびと", "人々", "people"),
            CharacterWord(1, "おとな", "大人", "adult"),
            CharacterWord(1, "ひとり", "一人", "one person"),
            CharacterWord(1, "ふたり", "二人", "two people"),

            CharacterWord(2, "だんせい", "男性", "male"),
            CharacterWord(2, "おとこ", "男", "man"),
            CharacterWord(2, "おとこのこ", "男の子", "boy"),

            CharacterWord(3, "じょせい", "女性", "female"),
            CharacterWord(3, "かのじょ", "彼女", "she"),
            CharacterWord(3, "おんな", "女", "woman"),
            CharacterWord(3, "おんなのこ", "女の子", "girl"),

            CharacterWord(4, "だんし", "男子", "boy"),
            CharacterWord(4, "じょし", "女子", "girl"),
            CharacterWord(4, "こども", "子供", "child"),
            CharacterWord(4, "おとこのこ", "男の子", "boy"),
            CharacterWord(4, "おんなのこ", "女の子", "girl"),

            CharacterWord(5, "でんしゃ", "電車", "train"),
            CharacterWord(5, "くるま", "車", "car"),

            CharacterWord(6, "やま", "山", "mountain"),

            CharacterWord(7, "かわ", "川", "river"),
            CharacterWord(7, "ナイルがわ", "ナイル川", "Nile River"),
            CharacterWord(7, "なかがわ", "中川", "Nakagawa River"),

            CharacterWord(8, "た", "田", "rice paddy"),
            CharacterWord(8, "たんぼ", "田んぼ", "rice field"),
            CharacterWord(8, "たなか", "田中", "Tanaka"),
            CharacterWord(8, "やまだ", "山田", "Yamada"),

            CharacterWord(9, "こめ", "米", "rice"),

            CharacterWord(10, "あめ", "雨", "rain"),

            CharacterWord(11, "いじょう", "以上", "more than"),
            CharacterWord(11, "じょうずな", "上手な", "skillful"),
            CharacterWord(11, "うえ", "上", "above"),
            CharacterWord(11, "うわぎ", "上着", "outerwear"),

            CharacterWord(12, "ちゅうがっこう", "中学校", "junior high school"),
            CharacterWord(12, "ちゅうがくせい", "中学生", "junior high school student"),
            CharacterWord(12, "ごぜんちゅう", "午前中", "in the morning"),
            CharacterWord(12, "せかいじゅう", "世界中", "all over the world"),
            CharacterWord(12, "なか", "中", "inside"),
            CharacterWord(12, "たなか", "田中", "Tanaka"),

            CharacterWord(13, "ちかてつ", "地下鉄", "subway"),
            CharacterWord(13, "した", "下", "below"),
            CharacterWord(13, "したぎ", "下着", "underwear"),
            CharacterWord(13, "へたな", "下手な", "unskillful"),

            CharacterWord(14, "ひだり", "左", "left"),

            CharacterWord(15, "みぎ", "右", "right"),

            CharacterWord(16, "せつめいする", "説明する", "to explain"),
            CharacterWord(16, "あかるい", "明るい", "bright"),
            CharacterWord(16, "あした", "明日", "tomorrow"),

            CharacterWord(17, "やすむ", "休む", "to rest"),
            CharacterWord(17, "やすみ", "休み", "rest"),
            CharacterWord(17, "ひるやすみ", "昼休み", "lunch break"),

            CharacterWord(18, "はやし", "林", "woods"),

            CharacterWord(19, "もり", "森", "forest"),

            CharacterWord(20, "すきな", "好きな", "liked"),
            CharacterWord(20, "だいすきな", "大好きな", "very liked"),

            CharacterWord(21, "いち", "一", "one"),
            CharacterWord(21, "いちがつ", "一月", "January"),
            CharacterWord(21, "いちじ", "一時", "one o'clock"),
            CharacterWord(21, "いちど", "一度", "once"),
            CharacterWord(21, "ついたち", "一日", "first day"),
            CharacterWord(21, "ひとつき", "一月", "one month"),
            CharacterWord(21, "ひとつ", "一つ", "one thing"),
            CharacterWord(21, "ひとり", "一人", "one person"),
            CharacterWord(21, "いちにち", "一日", "one day"),

            CharacterWord(22, "に", "二", "two"),
            CharacterWord(22, "にがつ", "二月", "February"),
            CharacterWord(22, "にじ", "二次", "secondary"),
            CharacterWord(22, "ふたつ", "二つ", "two things"),
            CharacterWord(22, "ふたり", "二人", "two people"),
            CharacterWord(22, "ふつか", "二日", "second day"),
            CharacterWord(22, "はつか", "二十日", "twentieth day"),
            CharacterWord(22, "はたち", "二十歳", "twenty years old"),
            CharacterWord(22, "はつか", "二十日", "twentieth day"),
            CharacterWord(22, "はたち", "二十歳", "twenty years old"),

            CharacterWord(23, "さん", "三", "three"),
            CharacterWord(23, "さんがつ", "三月", "March"),
            CharacterWord(23, "さんじ", "三時", "three o'clock"),
            CharacterWord(23, "さんにん", "三人", "three people"),
            CharacterWord(23, "みっつ", "三つ", "three things"),
            CharacterWord(23, "みっか", "三日", "third day"),

            CharacterWord(24, "し", "四", "four"),
            CharacterWord(24, "しがつ", "四月", "April"),
            CharacterWord(24, "よっつ", "四つ", "four things"),
            CharacterWord(24, "よっか", "四日", "fourth day"),
            CharacterWord(24, "よじ", "四時", "four o'clock"),
            CharacterWord(24, "よにん", "四人", "four people"),
            CharacterWord(24, "よんひゃく", "四百", "four hundred"),

            CharacterWord(25, "ご", "五", "five"),
            CharacterWord(25, "ごがつ", "五月", "May"),
            CharacterWord(25, "ごじ", "五時", "five o'clock"),
            CharacterWord(25, "ごにん", "五人", "five people"),
            CharacterWord(25, "いつか", "五日", "fifth day"),
            CharacterWord(25, "いつつ", "五つ", "five things"),

            CharacterWord(26, "ろく", "六", "six"),
            CharacterWord(26, "ろくがつ", "六月", "June"),
            CharacterWord(26, "ろくじ", "六時", "six o'clock"),
            CharacterWord(26, "ろくにん", "六人", "six people"),
            CharacterWord(26, "ろっぴゃく", "六百", "six hundred"),
            CharacterWord(26, "むっつ", "六つ", "six things"),
            CharacterWord(26, "むいか", "六日", "sixth day"),

            CharacterWord(27, "しち", "七", "seven"),
            CharacterWord(27, "しちがつ", "七月", "July"),
            CharacterWord(27, "しちじ", "七時", "seven o'clock"),
            CharacterWord(27, "しちにん", "七人", "seven people"),
            CharacterWord(27, "しちにん", "七人", "seven people"),
            CharacterWord(27, "ななつ", "七つ", "seven things"),
            CharacterWord(27, "なのか", "七日", "seventh day"),

            CharacterWord(28, "はち", "ハ", "eight"),
            CharacterWord(28, "はちがつ", "ハ月", "August"),
            CharacterWord(28, "はちじ", "ハ時", "eight o'clock"),
            CharacterWord(28, "はちにん", "ハ人", "eight people"),
            CharacterWord(28, "はっぴゃく", "ハ百", "eight hundred"),
            CharacterWord(28, "やっつ", "ハつ", "eight things"),
            CharacterWord(28, "ようか", "ハ日", "eighth day"),
            CharacterWord(28, "やおや", "ハ百屋", "greengrocer"),

            CharacterWord(29, "きゅう", "九", "nine"),
            CharacterWord(29, "きゅうにん", "九人", "nine people"),
            CharacterWord(29, "きゅうひゃく", "九百", "nine hundred"),
            CharacterWord(29, "くがつ", "九月", "September"),
            CharacterWord(29, "くじ", "九時", "nine o'clock"),
            CharacterWord(29, "くにん", "九人", "nine people"),
            CharacterWord(29, "ここのか", "九日", "ninth day"),
            CharacterWord(29, "ここのつ", "九つ", "nine things"),

            CharacterWord(30, "じゅう", "十", "ten"),
            CharacterWord(30, "じゅうじ", "十時", "ten o'clock"),
            CharacterWord(30, "じゅっぷん", "十分", "ten minutes"),
            CharacterWord(30, "じゅうがつ", "十月", "October"),
            CharacterWord(30, "じゅういちがつ", "十一月", "November"),
            CharacterWord(30, "じゅうにがつ", "十二月", "December"),
            CharacterWord(30, "じっぷん", "十分", "ten minutes"),
            CharacterWord(30, "とお", "十", "ten"),
            CharacterWord(30, "とおか", "十日", "tenth day"),
            CharacterWord(30, "はつか", "二十日", "twentieth day"),
            CharacterWord(30, "はたち", "二十歳", "twenty years old"),

            CharacterWord(31, "ひゃく", "百", "hundred"),
            CharacterWord(31, "さんびゃく", "三百", "three hundred"),
            CharacterWord(31, "ろっぴゃく", "六百", "six hundred"),
            CharacterWord(31, "はっぴゃく", "ハ百", "eight hundred"),
            CharacterWord(31, "やおや", "ハ百屋", "greengrocer"),

            CharacterWord(32, "せん", "千", "thousand"),
            CharacterWord(32, "せんえん", "千円", "one thousand yen"),
            CharacterWord(32, "さんぜん", "三千", "three thousand"),
            CharacterWord(32, "はっせん", "ハ千", "eight thousand"),

            CharacterWord(33, "いちまん", "一万", "ten thousand"),
            CharacterWord(33, "ひゃくまん", "百万", "one million"),

            CharacterWord(34, "えん", "円", "yen"),
            CharacterWord(34, "～えん", "～円", "~ yen"),

            CharacterWord(35, "いろ", "色", "color"),

            CharacterWord(36, "しろ", "白", "white"),
            CharacterWord(36, "しろい", "白い", "white"),

            CharacterWord(37, "くろ", "黒", "black"),
            CharacterWord(37, "くろい", "黒い", "black"),

            CharacterWord(38, "あか", "赤", "red"),
            CharacterWord(38, "あかちゃん", "赤ちゃん", "baby"),
            CharacterWord(38, "あかい", "赤い", "red"),

            CharacterWord(39, "あお", "青", "blue"),
            CharacterWord(39, "あおい", "青い", "blue"),

            CharacterWord(40, "きいろ", "黄色", "yellow"),
            CharacterWord(40, "きいろい", "黄色い", "yellow"),

            CharacterWord(41, "げつようび", "月曜日", "Monday"),
            CharacterWord(41, "せんげつ", "先月", "last month"),
            CharacterWord(41, "まいにち", "毎日", "every day"),
            CharacterWord(41, "いちがつ", "一月", "January"),
            CharacterWord(41, "しょうがつ", "正月", "New Year's Day"),
            CharacterWord(41, "つき", "月", "moon"),
            CharacterWord(41, "まいつき", "毎月", "every month"),
            CharacterWord(41, "ひとつき", "一月", "one month"),

            CharacterWord(42, "かようび", "火曜日", "Tuesday"),
            CharacterWord(42, "かじ", "火事", "fire"),
            CharacterWord(42, "ひ", "火", "fire"),

            CharacterWord(43, "すいようび", "水曜日", "Wednesday"),
            CharacterWord(43, "すいえい", "水泳", "swimming"),
            CharacterWord(43, "すいどう", "水道", "water supply"),
            CharacterWord(43, "みず", "水", "water"),

            CharacterWord(44, "もくようび", "木曜日", "Thursday"),
            CharacterWord(44, "き", "木", "tree"),

            CharacterWord(45, "きんようび", "金曜日", "Friday"),
            CharacterWord(45, "おかね", "お金", "money"),
            CharacterWord(45, "おかねもちの", "お金持ちの", "rich"),

            CharacterWord(46, "どようび", "土曜日", "Saturday"),
            CharacterWord(46, "おみやげ", "お土産", "souvenir"),

            CharacterWord(47, "にちようび", "日曜日", "Sunday"),
            CharacterWord(47, "いちにち", "一日", "one day"),
            CharacterWord(47, "まいにち", "毎日", "every day"),
            CharacterWord(47, "ひ", "日", "day"),
            CharacterWord(47, "ふつか", "二日", "second day"),
            CharacterWord(47, "みっか", "三日", "third day"),
            CharacterWord(47, "きのう", "昨日", "yesterday"),
            CharacterWord(47, "きょう", "今日", "today"),
            CharacterWord(47, "あした", "明日", "tomorrow"),
            CharacterWord(47, "ついたち", "一日", "first day"),

            CharacterWord(48, "～ようび", "～曜日", "~ day of the week"),

            CharacterWord(49, "まいにち", "毎日", "every day"),
            CharacterWord(49, "まいしゅう", "毎週", "every week"),
            CharacterWord(49, "まいあさ", "毎朝", "every morning"),
            CharacterWord(49, "まいばん", "毎晩", "every evening"),

            CharacterWord(50, "～しゅうかん", "～週間", "~ week(s)"),
            CharacterWord(50, "せんしゅう", "先週", "last week"),
            CharacterWord(50, "こんしゅう", "今週", "this week"),
            CharacterWord(50, "らいしゅう", "来週", "next week"),

            CharacterWord(51, "ぎんこう", "銀行", "bank"),
            CharacterWord(51, "りょこうする", "旅行する", "to travel"),
            CharacterWord(51, "きゅうこう", "急行", "express train"),
            CharacterWord(51, "ひこうき", "飛行機", "airplane"),
            CharacterWord(51, "いく", "行く", "to go"),
            CharacterWord(51, "おこなう", "行う", "to do"),

            CharacterWord(52, "らいしゅう", "来週", "next week"),
            CharacterWord(52, "らいげつ", "来月", "next month"),
            CharacterWord(52, "らいねん", "来年", "next year"),
            CharacterWord(52, "くる", "来る", "to come"),
            CharacterWord(52, "きます", "来ます", "will come"),
            CharacterWord(52, "こない", "来ない", "will not come"),

            CharacterWord(53, "かえる", "帰る", "to return"),

            CharacterWord(54, "はじまる", "始まる", "to begin"),
            CharacterWord(54, "はじめる", "始める", "to start"),

            CharacterWord(55, "おわる", "終わる", "to end"),

            CharacterWord(56, "おきる", "起きる", "to wake up"),
            CharacterWord(56, "おこす", "起こす", "to wake (someone) up"),

            CharacterWord(57, "ねる", "寝る", "to sleep"),

            CharacterWord(58, "はたらく", "働く", "to work"),

            CharacterWord(59, "べんきょうする", "勉強する", "to study"),

            CharacterWord(60, "べんきょうする", "勉強する", "to study"),
            CharacterWord(60, "つよい", "強い", "strong"),

            CharacterWord(61, "わたし", "私", "I"),
            CharacterWord(61, "わたくし", "私", "I (formal)"),

            CharacterWord(62, "かぞく", "家族", "family"),
            CharacterWord(62, "かない", "家内", "my wife"),
            CharacterWord(62, "かてい", "家庭", "home"),
            CharacterWord(62, "いえ", "家", "house"),

            CharacterWord(63, "かぞく", "家族", "family"),

            CharacterWord(64, "そふ", "祖父", "grandfather"),
            CharacterWord(64, "ちち", "父", "father"),
            CharacterWord(64, "おとうさん", "お父さん", "father (polite)"),
            CharacterWord(64, "おじ", "叔父", "uncle (younger than parent)"),
            CharacterWord(64, "おじ", "伯父", "uncle (older than parent)"),

            CharacterWord(65, "そぼ", "祖母", "grandmother"),
            CharacterWord(65, "はは", "母", "mother"),
            CharacterWord(65, "おかあさん", "お母さん", "mother (polite)"),
            CharacterWord(65, "おば", "叔母", "aunt (younger than parent)"),
            CharacterWord(65, "おば", "伯母", "aunt (older than parent)"),

            CharacterWord(66, "きょうだい", "兄弟", "siblings"),
            CharacterWord(66, "あに", "兄", "older brother"),
            CharacterWord(66, "おにいさん", "お兄さん", "older brother (polite)"),

            CharacterWord(67, "きょうだい", "兄弟", "siblings"),
            CharacterWord(67, "おとうと", "弟", "younger brother"),

            CharacterWord(68, "あね", "姉", "older sister"),
            CharacterWord(68, "おねえさん", "お姉さん", "older sister (polite)"),

            CharacterWord(69, "いもうと", "妹", "younger sister"),

            CharacterWord(70, "しゅじん", "主人", "husband"),
            CharacterWord(70, "ごしゅじん", "ご主人", "husband (polite)"),
            CharacterWord(70, "しゅふ", "主婦", "housewife"),

            CharacterWord(71, "かない", "家内", "my wife"),
            CharacterWord(71, "あんあいする", "案内する", "to guide"),
            CharacterWord(71, "いない", "以内", "within"),

            CharacterWord(72, "おくさん", "奥さん", "wife (polite)"),

            CharacterWord(73, "しごと", "仕事", "job"),
            CharacterWord(73, "しかた", "仕方", "way of doing"),

            CharacterWord(74, "しょくじ", "食事", "meal"),
            CharacterWord(74, "ようじ", "用事", "errand"),
            CharacterWord(74, "かじ", "家事", "housework"),
            CharacterWord(74, "だいじな", "大事な", "important"),
            CharacterWord(74, "しごと", "仕事", "work"),

            CharacterWord(75, "せんせい", "先生", "teacher"),
            CharacterWord(75, "がくせい", "学生", "student"),
            CharacterWord(75, "せいと", "生徒", "pupil"),
            CharacterWord(75, "いっしょうけんめいに", "一生懸命に", "earnestly"),
            CharacterWord(75, "たんじょうび", "誕生日", "birthday"),
            CharacterWord(75, "いきる", "生きる", "to live"),
            CharacterWord(75, "うまれる", "生まれる", "to be born"),

            CharacterWord(76, "せんせい", "先生", "teacher"),
            CharacterWord(76, "せんげつ", "先月", "last month"),
            CharacterWord(76, "せんしゅう", "先週", "last week"),
            CharacterWord(76, "さきに", "先に", "ahead"),

            CharacterWord(77, "がくせい", "学生", "student"),
            CharacterWord(77, "がっこう", "学校", "school"),
            CharacterWord(77, "りゅうがくせい", "留学生", "international student"),
            CharacterWord(77, "だいがく", "大学", "university"),

            CharacterWord(78, "かいしゃ", "会社", "company"),
            CharacterWord(78, "かいわ", "会話", "conversation"),
            CharacterWord(78, "かいぎ", "会議", "meeting"),
            CharacterWord(78, "あう", "会う", "to meet"),

            CharacterWord(79, "かいしゃ", "会社", "company"),
            CharacterWord(79, "しゃかい", "社会", "society"),
            CharacterWord(79, "じんじゃ", "神社", "shrine"),

            CharacterWord(80, "かいしゃいん", "会社員", "company employee"),
            CharacterWord(80, "しゃいん", "社員", "company employee"),
            CharacterWord(80, "てんいん", "店員", "shop assistant"),
            CharacterWord(80, "えきいん", "駅員", "station attendant"),

            CharacterWord(81, "じかん", "時間", "time"),
            CharacterWord(81, "～じかん", "～時間", "~ hour(s)"),
            CharacterWord(81, "～じ", "～時", "~ o'clock"),
            CharacterWord(81, "とき", "時", "time"),
            CharacterWord(81, "とけい", "時計", "clock"),

            CharacterWord(82, "～ふん", "～分", "~ minute(s)"),
            CharacterWord(82, "はんぶんの", "半分の", "half"),
            CharacterWord(82, "じぶんで", "自分で", "by oneself"),
            CharacterWord(82, "わかる", "分かる", "to understand"),

            CharacterWord(83, "ごぜん", "午前", "a.m."),
            CharacterWord(83, "ごご", "午後", "p.m."),

            CharacterWord(84, "ごぜん", "午前", "a.m."),
            CharacterWord(84, "ごぜんちゅう", "午前中", "in the morning"),
            CharacterWord(84, "まえ", "前", "before"),
            CharacterWord(84, "なまえ", "名前", "name"),
            CharacterWord(84, "～ねんまえ", "～年前", "~ years ago"),

            CharacterWord(85, "ごご", "午後", "p.m."),
            CharacterWord(85, "さいごの", "最後の", "last"),
            CharacterWord(85, "あとで", "後で", "later"),
            CharacterWord(85, "うしろ", "後ろ", "behind"),

            CharacterWord(86, "じかん", "時間", "time"),
            CharacterWord(86, "～じかん", "～時間", "~ hour(s)"),
            CharacterWord(86, "あいだ", "間", "interval"),
            CharacterWord(86, "このあいだ", "この間", "the other day"),
            CharacterWord(86, "まにあう", "間に合う", "to be on time"),

            CharacterWord(87, "～じはん", "～時半", "~ half past ..."),
            CharacterWord(87, "はんぶんの", "半分の", "half"),

            CharacterWord(88, "あさ", "朝", "morning"),
            CharacterWord(88, "あさごはん", "朝ご飯", "breakfast"),
            CharacterWord(88, "まいあさ", "毎朝", "every morning"),
            CharacterWord(88, "けさ", "今朝", "this morning"),

            CharacterWord(89, "ひる", "昼", "noon"),
            CharacterWord(89, "ひるま", "昼間", "daytime"),
            CharacterWord(89, "ひるごはん", "昼ご飯", "lunch"),
            CharacterWord(89, "ひるやすみ", "昼休み", "lunch break"),

            CharacterWord(90, "ばん", "晩", "evening"),
            CharacterWord(90, "こんばん", "今晩", "this evening"),
            CharacterWord(90, "まいばん", "毎晩", "every evening"),
            CharacterWord(90, "ばんごはん", "晩ご飯", "dinner"),

            CharacterWord(91, "こんしゅう", "今週", "this week"),
            CharacterWord(91, "こんげつ", "今月", "this month"),
            CharacterWord(91, "こんばん", "今晩", "this evening"),
            CharacterWord(91, "こんど", "今度", "next time"),
            CharacterWord(91, "いま", "今", "now"),
            CharacterWord(91, "きょう", "今日", "today"),
            CharacterWord(91, "ことし", "今年", "this year"),
            CharacterWord(91, "けさ", "今朝", "this morning"),

            CharacterWord(92, "きょねん", "去年", "last year"),

            CharacterWord(93, "～ねん", "～年", "~ year(s)"),
            CharacterWord(93, "きょねん", "去年", "last year"),
            CharacterWord(93, "らいねん", "来年", "next year"),
            CharacterWord(93, "まいねん", "毎年", "every year"),
            CharacterWord(93, "とし", "年", "year"),
            CharacterWord(93, "ことし", "今年", "this year"),
            CharacterWord(93, "まいとし", "毎年", "every year"),

            CharacterWord(94, "ゆうがた", "夕方", "evening"),

            CharacterWord(95, "りょうほうの", "両方の", "both"),
            CharacterWord(95, "このかた", "この方", "this person (polite)"),
            CharacterWord(95, "ゆうがた", "夕方", "evening"),
            CharacterWord(95, "しかた", "仕方", "way of doing"),
            CharacterWord(95, "～かた", "～方", "~ way of"),

            CharacterWord(96, "はる", "春", "spring"),
            CharacterWord(96, "はるやすみ", "春休み", "spring vacation"),

            CharacterWord(97, "なつ", "夏", "summer"),
            CharacterWord(97, "なつやすみ", "夏休み", "summer vacation"),

            CharacterWord(98, "あき", "秋", "autumn"),

            CharacterWord(99, "ふゆ", "冬", "winter"),
            CharacterWord(99, "ふゆやすみ", "冬休み", "winter vacation"),

            CharacterWord(100, "こんや", "今夜", "tonight"),
            CharacterWord(100, "よる", "夜", "night")
        )

        val readings = listOf(
            CharacterReading(1, "ジン"),
            CharacterReading(1, "ニン"),
            CharacterReading(1, "ひと"),

            CharacterReading(2, "ダン"),
            CharacterReading(2, "おとこ"),

            CharacterReading(3, "ジョ"),
            CharacterReading(3, "おんな"),

            CharacterReading(4, "シ"),
            CharacterReading(4, "こ"),

            CharacterReading(5, "シャ"),
            CharacterReading(5, "くるま"),

            CharacterReading(6, "やま"),

            CharacterReading(7, "かわ"),

            CharacterReading(8, "た"),

            CharacterReading(9, "こめ"),

            CharacterReading(10, "あめ"),

            CharacterReading(11, "ジョウ"),
            CharacterReading(11, "うえ"),
            CharacterReading(11, "うわ"),

            CharacterReading(12, "チュウ"),
            CharacterReading(12, "ジュウ"),
            CharacterReading(12, "なか"),

            CharacterReading(13, "カ"),
            CharacterReading(13, "した"),

            CharacterReading(14, "ひだり"),

            CharacterReading(15, "みぎ"),

            CharacterReading(16, "メイ"),
            CharacterReading(16, "あか"),

            CharacterReading(17, "やす"),

            CharacterReading(18, "はやし"),

            CharacterReading(19, "もり"),

            CharacterReading(20, "す"),

            CharacterReading(21, "イチ"),
            CharacterReading(21, "ひと"),

            CharacterReading(22, "ニ"),
            CharacterReading(22, "ふた"),

            CharacterReading(23, "サン"),
            CharacterReading(23, "みっつ"),

            CharacterReading(24, "シ"),
            CharacterReading(24, "よ"),
            CharacterReading(24, "よっ"),
            CharacterReading(24, "よん"),

            CharacterReading(25, "ゴ"),
            CharacterReading(25, "いつ"),

            CharacterReading(26, "ロク"),
            CharacterReading(26, "むっつ"),
            CharacterReading(26, "むい"),

            CharacterReading(27, "シチ"),
            CharacterReading(27, "なな"),
            CharacterReading(27, "なの"),

            CharacterReading(28, "ハチ"),
            CharacterReading(28, "やっ"),
            CharacterReading(28, "よう"),

            CharacterReading(29, "キュウ"),
            CharacterReading(29, "ク"),
            CharacterReading(29, "ここの"),

            CharacterReading(30, "ジュウ"),
            CharacterReading(30, "ジッ"),
            CharacterReading(30, "とお"),

            CharacterReading(31, "ヒャク"),

            CharacterReading(32, "セン"),

            CharacterReading(33, "マン"),

            CharacterReading(34, "エン"),

            CharacterReading(35, "いろ"),

            CharacterReading(36, "しろ"),

            CharacterReading(37, "くろ"),

            CharacterReading(38, "あか"),

            CharacterReading(39, "あお"),

            CharacterReading(40, "き"),

            CharacterReading(41, "ゲツ"),
            CharacterReading(41, "ガツ"),
            CharacterReading(41, "つき"),

            CharacterReading(42, "カ"),
            CharacterReading(42, "ひ"),

            CharacterReading(43, "スイ"),
            CharacterReading(43, "みず"),

            CharacterReading(44, "モク"),
            CharacterReading(44, "き"),

            CharacterReading(45, "キン"),
            CharacterReading(45, "かね"),

            CharacterReading(46, "ド"),

            CharacterReading(47, "ニチ"),
            CharacterReading(47, "ひ"),
            CharacterReading(47, "か"),

            CharacterReading(48, "ヨウ"),

            CharacterReading(49, "マイ"),

            CharacterReading(50, "シュウ"),

            CharacterReading(51, "コウ"),
            CharacterReading(51, "い"),
            CharacterReading(51, "おこな"),

            CharacterReading(52, "ライ"),
            CharacterReading(52, "く"),

            CharacterReading(53, "かえ"),

            CharacterReading(54, "はじ"),

            CharacterReading(55, "お"),

            CharacterReading(56, "お"),

            CharacterReading(57, "ね"),

            CharacterReading(58, "はたら"),

            CharacterReading(59, "ベン"),

            CharacterReading(60, "キョウ"),
            CharacterReading(60, "つよ"),

            CharacterReading(61, "わたし"),
            CharacterReading(61, "わたくし"),

            CharacterReading(62, "カ"),
            CharacterReading(62, "いえ"),

            CharacterReading(63, "ゾク"),

            CharacterReading(64, "フ"),
            CharacterReading(64, "ちち"),
            CharacterReading(64, "とう"),

            CharacterReading(65, "ボ"),
            CharacterReading(65, "はは"),
            CharacterReading(65, "かあ"),

            CharacterReading(66, "キョウ"),
            CharacterReading(66, "あに"),
            CharacterReading(66, "にい"),

            CharacterReading(67, "ダイ"),
            CharacterReading(67, "おとうと"),

            CharacterReading(68, "あね"),
            CharacterReading(68, "ねえ"),

            CharacterReading(69, "いもうと"),

            CharacterReading(70, "シュ"),

            CharacterReading(71, "ナイ"),

            CharacterReading(72, "おく"),

            CharacterReading(73, "シ"),

            CharacterReading(74, "ジ"),
            CharacterReading(74, "こと"),

            CharacterReading(75, "セイ"),
            CharacterReading(75, "ショウ"),
            CharacterReading(75, "い"),
            CharacterReading(75, "う"),

            CharacterReading(76, "セン"),
            CharacterReading(76, "さき"),

            CharacterReading(77, "ガク"),

            CharacterReading(78, "カイ"),
            CharacterReading(78, "あ"),

            CharacterReading(79, "シャ"),

            CharacterReading(80, "イン"),

            CharacterReading(81, "ジ"),
            CharacterReading(81, "とき"),
            CharacterReading(81, "と"),

            CharacterReading(82, "フン"),
            CharacterReading(82, "ブン"),
            CharacterReading(82, "わ"),

            CharacterReading(83, "ゴ"),

            CharacterReading(84, "ゼン"),
            CharacterReading(84, "まえ"),

            CharacterReading(85, "ゴ"),
            CharacterReading(85, "あと"),
            CharacterReading(85, "うし"),

            CharacterReading(86, "カン"),
            CharacterReading(86, "あいだ"),
            CharacterReading(86, "ま"),

            CharacterReading(87, "ハン"),

            CharacterReading(88, "あさ"),

            CharacterReading(89, "ひる"),

            CharacterReading(90, "バン"),

            CharacterReading(91, "コン"),
            CharacterReading(91, "いま"),

            CharacterReading(92, "キョ"),

            CharacterReading(93, "ネン"),
            CharacterReading(93, "とし"),

            CharacterReading(94, "ゆう"),

            CharacterReading(95, "ホウ"),
            CharacterReading(95, "かた"),

            CharacterReading(96, "はる"),

            CharacterReading(97, "なつ"),

            CharacterReading(98, "あき"),

            CharacterReading(99, "ふゆ"),

            CharacterReading(100, "ヤ"),
            CharacterReading(100, "よる")
        )

        val radicals = listOf(
            Radical(1, "人", "person"),
            Radical(2, "女", "woman"),
            Radical(3, "子", "child"),
            Radical(4, "車", "car"),
            Radical(5, "山", "mountain"),
            Radical(6, "田", "rice field"),
            Radical(7, "雨", "rain"),
            Radical(8, "上", "above"),
            Radical(9, "下", "below"),
            Radical(10, "林", "woods"),
            Radical(11, "森", "forest"),
            Radical(12, "一", "one"),
            Radical(13, "二", "two"),
            Radical(14, "三", "three"),
            Radical(15, "五", "five"),
            Radical(16, "六", "six"),
            Radical(17, "七", "seven"),
            Radical(18, "ハ", "eight"),
            Radical(19, "九", "nine"),
            Radical(20, "円", "yen"),
            Radical(21, "月", "moon"),
            Radical(22, "水", "water"),
            Radical(23, "土", "earth"),
            Radical(24, "日", "day"),
            Radical(25, "母", "mother"),
            Radical(26, "事", "matter"),
            Radical(27, "生", "life"),
            Radical(28, "社", "company"),
            Radical(29, "方", "direction"),

            Radical(30, "力", "power"),
            Radical(31, "ノ", "on"),
            Radical(32, "丨", "stick"),
            Radical(33, "丷", "eight"),
            Radical(34, "口", "mouth"),
            Radical(35, "𠂇", "ten"),
            Radical(36, "工", "work"),
            Radical(37, "亻", "man"),
            Radical(38, "儿", "legs"),
            Radical(39, "囗", "box"),
            Radical(40, "勹", "wrap"),
            Radical(41, "巴", "comma"),
            Radical(42, "⺈", "corner"),
            Radical(43, "里", "village"),
            Radical(44, "灬", "fire"),
            Radical(45, "龶", "master"),
            Radical(46, "艹", "grass"),
            Radical(47, "𠆢", "man"),
            Radical(48, "隹", "little bird"),
            Radical(49, "习", "to yell"),
            Radical(50, "𠂉", "man"),
            Radical(51, "冂", "wide"),
            Radical(52, "⻌", "move"),
            Radical(53, "彳", "step"),
            Radical(54, "丁", "block"),
            Radical(55, "未", "not yet"),
            Radical(56, "刂", "sword"),
            Radical(57, "⺕", "to yell"),
            Radical(58, "冖", "cover"),
            Radical(59, "巾", "cloth"),
            Radical(60, "ム", "private"),
            Radical(61, "糸", "thread"),
            Radical(62, "⺀", "ice"),
            Radical(63, "夂", "go"),
            Radical(64, "己", "self"),
            Radical(65, "疋", "roll of cloth"),
            Radical(66, "重", "heavy"),
            Radical(67, "丶", "dot"),
            Radical(68, "弓", "bow"),
            Radical(69, "禾", "grain"),
            Radical(70, "豕", "pig"),
            Radical(71, "宀", "roof"),
            Radical(72, "方", "way"),
            Radical(73, "乂", "crossed swords"),
            Radical(74, "亠", "lid"),
            Radical(75, "⺌", "small"),
            Radical(76, "礻", "to show"),
            Radical(77, "寸", "inch"),
            Radical(78, "刀", "sword"),
            Radical(79, "干", "dry"),
            Radical(80, "⺼", "body part"),
            Radical(81, "䒑", "grass"),
            Radical(82, "門", "gate"),
            Radical(83, "尺", "foot (length)"),
            Radical(84, "㇇", "fu"),
            Radical(85, "ヰ", "wheel around"),
            Radical(86, "タ", "ta"),
            Radical(87, "小", "small"),
            Radical(88, "共", "together"),
            Radical(89, "王", "man"),
            Radical(90, "翟", "little bird"),
            Radical(91, "毋", "mother"),
            Radical(92, "周", "circumference"),
            Radical(93, "帚", "broom"),
            Radical(94, "台", "pedestal"),
            Radical(95, "冬", "winter"),
            Radical(96, "走", "run"),
            Radical(97, "寝", "sleep"),
            Radical(98, "動", "to move"),
            Radical(99, "免", "escape"),
            Radical(100, "虫", "insect"),
            Radical(101, "矢", "arrow"),
            Radical(102, "弔", "to mourn"),
            Radical(103, "市", "market"),
            Radical(104, "王", "king"),
            Radical(105, "米", "rice"),
            Radical(106, "士", "samurai"),
            Radical(107, "⺧", "on the ground"),
            Radical(108, "貝", "shellfish"),
            Radical(109, "寺", "temple"),
            Radical(110, "幺", "short thread"),
            Radical(111, "𠦝", "ten times earlier"),
            Radical(112, "旦", "dawn"),
            Radical(113, "亼", "assembly"),
            Radical(114, "𡗗", "three men"),
            Radical(115, "自", "self")
        )

        val components = listOf(
            Component(1, 1, Position.OTHER),

            Component(2, 6, Position.UP),
            Component(2, 30, Position.DOWN),

            Component(3, 2, Position.OTHER),

            Component(4, 3, Position.OTHER),

            Component(5, 4, Position.OTHER),

            Component(6, 5, Position.OTHER),

            Component(7, 31, Position.LEFT),
            Component(7, 32, Position.OTHER),
            Component(7, 32, Position.RIGHT),

            Component(8, 6, Position.OTHER),

            Component(9, 33, Position.OTHER),
            Component(9, 12, Position.OTHER),
            Component(9, 1, Position.OTHER),
            Component(9, 32, Position.OTHER),

            Component(10, 7, Position.OTHER),

            Component(11, 8, Position.OTHER),

            Component(12, 32, Position.OTHER),
            Component(12, 34, Position.OTHER),

            Component(13, 9, Position.OTHER),

            Component(14, 35, Position.HANGING),
            Component(14, 36, Position.OTHER),

            Component(15, 35, Position.HANGING),
            Component(15, 34, Position.OTHER),

            Component(16, 24, Position.LEFT),
            Component(16, 21, Position.RIGHT),

            Component(17, 37, Position.LEFT),
            Component(17, 12, Position.RIGHT),
            Component(17, 1, Position.RIGHT),
            Component(17, 32, Position.RIGHT),

            Component(18, 10, Position.OTHER),

            Component(19, 11, Position.OTHER),

            Component(20, 2, Position.LEFT),
            Component(20, 3, Position.RIGHT),

            Component(21, 12, Position.OTHER),

            Component(22, 13, Position.OTHER),

            Component(23, 14, Position.OTHER),

            Component(24, 38, Position.OTHER),
            Component(24, 39, Position.OUTSIDE),

            Component(25, 15, Position.OTHER),

            Component(26, 16, Position.OTHER),

            Component(27, 17, Position.OTHER),

            Component(28, 18, Position.OTHER),

            Component(29, 19, Position.OTHER),

            Component(30, 12, Position.OTHER),
            Component(30, 32, Position.OTHER),

            Component(31, 12, Position.UP),
            Component(31, 24, Position.DOWN),
            Component(31, 31, Position.DOWN),

            Component(32, 31, Position.UP),
            Component(32, 12, Position.DOWN),
            Component(32, 32, Position.DOWN),

            Component(33, 12, Position.UP),
            Component(33, 40, Position.DOWN),

            Component(34, 20, Position.OTHER),

            Component(35, 41, Position.DOWN),
            Component(35, 42, Position.UP),

            Component(36, 31, Position.UP),
            Component(36, 24, Position.DOWN),

            Component(37, 43, Position.UP),
            Component(37, 44, Position.DOWN),

            Component(38, 23, Position.UP),
            Component(38, 87, Position.DOWN),
            Component(38, 31, Position.DOWN),

            Component(39, 45, Position.UP),
            Component(39, 21, Position.DOWN),

            Component(40, 6, Position.OTHER),
            Component(40, 88, Position.OTHER),

            Component(41, 21, Position.OTHER),

            Component(42, 33, Position.OUTSIDE),
            Component(42, 1, Position.OTHER),

            Component(43, 22, Position.OTHER),

            Component(44, 12, Position.OTHER),
            Component(44, 32, Position.OTHER),
            Component(44, 1, Position.OTHER),

            Component(45, 47, Position.UP),
            Component(45, 89, Position.DOWN),
            Component(45, 33, Position.DOWN),

            Component(46, 23, Position.OTHER),

            Component(47, 24, Position.OTHER),

            Component(48, 24, Position.LEFT),
            Component(48, 90, Position.RIGHT),

            Component(49, 50, Position.UP),
            Component(49, 91, Position.DOWN),

            Component(50, 52, Position.CHAIR),
            Component(50, 92, Position.OTHER),

            Component(51, 53, Position.LEFT),
            Component(51, 54, Position.RIGHT),
            Component(51, 12, Position.RIGHT),

            Component(52, 33, Position.OTHER),
            Component(52, 55, Position.OTHER),

            Component(53, 56, Position.LEFT),
            Component(53, 93, Position.RIGHT),

            Component(54, 2, Position.LEFT),
            Component(54, 94, Position.RIGHT),

            Component(55, 61, Position.LEFT),
            Component(55, 95, Position.RIGHT),

            Component(56, 64, Position.OTHER),
            Component(56, 96, Position.CHAIR),

            Component(57, 97, Position.OTHER),

            Component(58, 37, Position.LEFT),
            Component(58, 98, Position.RIGHT),

            Component(59, 30, Position.OTHER),
            Component(59, 99, Position.CHAIR),

            Component(60, 68, Position.LEFT),
            Component(60, 60, Position.RIGHT),
            Component(60, 100, Position.RIGHT),

            Component(61, 69, Position.LEFT),
            Component(61, 60, Position.RIGHT),

            Component(62, 71, Position.UP),
            Component(62, 70, Position.DOWN),

            Component(63, 29, Position.LEFT),
            Component(63, 50, Position.RIGHT),
            Component(63, 101, Position.RIGHT),

            Component(64, 18, Position.UP),
            Component(64, 73, Position.DOWN),

            Component(65, 25, Position.OTHER),

            Component(66, 34, Position.UP),
            Component(66, 38, Position.DOWN),

            Component(67, 33, Position.UP),
            Component(67, 102, Position.OUTSIDE),
            Component(67, 31, Position.DOWN),

            Component(68, 2, Position.LEFT),
            Component(68, 103, Position.RIGHT),

            Component(69, 2, Position.LEFT),
            Component(69, 55, Position.RIGHT),

            Component(70, 67, Position.UP),
            Component(70, 104, Position.DOWN),

            Component(71, 51, Position.OTHER),
            Component(71, 1, Position.OTHER),

            Component(72, 51, Position.OUTSIDE),
            Component(72, 105, Position.OTHER),
            Component(72, 12, Position.DOWN),
            Component(72, 1, Position.DOWN),
            Component(72, 31, Position.UP),

            Component(73, 37, Position.LEFT),
            Component(73, 106, Position.RIGHT),

            Component(74, 26, Position.OTHER),

            Component(75, 27, Position.OTHER),

            Component(76, 107, Position.UP),
            Component(76, 38, Position.DOWN),

            Component(77, 58, Position.UP),
            Component(77, 75, Position.UP),
            Component(77, 3, Position.DOWN),

            Component(78, 47, Position.UP),
            Component(78, 13, Position.DOWN),
            Component(78, 60, Position.DOWN),

            Component(79, 76, Position.LEFT),
            Component(79, 23, Position.RIGHT),

            Component(80, 34, Position.UP),
            Component(80, 108, Position.DOWN),

            Component(81, 24, Position.LEFT),
            Component(81, 109, Position.RIGHT),

            Component(82, 18, Position.UP),
            Component(82, 78, Position.DOWN),

            Component(83, 31, Position.HANGING),
            Component(83, 79, Position.OTHER),

            Component(84, 81, Position.UP),
            Component(84, 80, Position.DOWN),
            Component(84, 56, Position.DOWN),

            Component(85, 53, Position.LEFT),
            Component(85, 110, Position.RIGHT),
            Component(85, 63, Position.RIGHT),

            Component(86, 82, Position.OUTSIDE),
            Component(86, 24, Position.OTHER),

            Component(87, 33, Position.UP),
            Component(87, 13, Position.OTHER),
            Component(87, 32, Position.OTHER),

            Component(88, 111, Position.LEFT),
            Component(88, 21, Position.RIGHT),

            Component(89, 83, Position.OUTSIDE),
            Component(89, 112, Position.OTHER),

            Component(90, 24, Position.LEFT),
            Component(90, 99, Position.RIGHT),

            Component(91, 113, Position.UP),
            Component(91, 84, Position.DOWN),

            Component(92, 23, Position.UP),
            Component(92, 60, Position.DOWN),

            Component(93, 50, Position.UP),
            Component(93, 85, Position.DOWN),

            Component(94, 86, Position.OTHER),

            Component(95, 29, Position.OTHER),

            Component(96, 114, Position.UP),
            Component(96, 24, Position.DOWN),

            Component(97, 12, Position.UP),
            Component(97, 115, Position.UP),
            Component(97, 63, Position.DOWN),

            Component(98, 69, Position.LEFT),
            Component(98, 1, Position.RIGHT),
            Component(98, 33, Position.RIGHT),

            Component(99, 63, Position.UP),
            Component(99, 62, Position.DOWN),

            Component(100, 74, Position.UP),
            Component(100, 37, Position.LEFT),
            Component(100, 67, Position.OUTSIDE),
            Component(100, 63, Position.OUTSIDE)
        )

        /*val songs = listOf(

        )

        val SongCharacters = listOf(

        )*/

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            Log.d("RoomDatabase", "getDatabase...")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "KanjiDB.db"
                )
                    .fallbackToDestructiveMigration()
                    .addCallback(object : RoomDatabase.Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            Log.d("RoomDatabase", "onCreate...")

                            CoroutineScope(Dispatchers.IO).launch {
                                Log.d("RoomDatabase", "Insertando...")
                                INSTANCE?.let { database ->

                                    database.kanjiDao().insertCharacterBatch(characters)
                                    database.kanjiDao().insertCharacterWordBatch(words)
                                    database.kanjiDao().insertRadicalBatch(radicals)
                                    database.kanjiDao().insertCharacterReadingBatch(readings)
                                    database.kanjiDao().insertComponentBatch(components)
                                    //database.kanjiDao()?.insertSongBatch(songs)
                                    //database.kanjiDao()?.insertSongCharacterBatch(SongCharacters)
                                }
                            }
                        }
                    })
                    .build()

                val dbFile = context.getDatabasePath("KanjiDB")
                Log.d("RoomDatabase", "Database file path: ${dbFile.absolutePath}")

                INSTANCE = instance
                instance
            }
        }
    }
}