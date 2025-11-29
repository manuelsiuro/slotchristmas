package com.slotchristmas.util

object FestiveMessages {
    private val MESSAGES = listOf(
        "Ho ho ho! Merry Christmas!",
        "May your gifts bring joy!",
        "Santa approved this spin!",
        "'Tis the season of giving!",
        "Spreading Christmas cheer!",
        "Jingle all the way!",
        "What a wonderful gift!",
        "Christmas magic in action!",
        "Deck the halls with joy!",
        "Let it snow, let it spin!",
        "Warm wishes and happy spins!",
        "The spirit of giving!",
        "Holiday miracles await!",
        "Fa la la la la!",
        "Season's greetings!",
        "A gift from the heart!",
        "Christmas joy is here!",
        "Happy holidays!",
        "Wishing you the best!",
        "Celebrate with love!",
        "Making spirits bright!",
        "Santa's little helper!",
        "Unwrap the happiness!",
        "Christmas wishes come true!",
        "Joy to the world!"
    )

    fun random(): String = MESSAGES.random()
}
