package com.consolebuilder.domain

import eu.timepit.refined.types.string.NonEmptyString

trait InteractiveMessage

case class TextMessage(s: NonEmptyString) extends InteractiveMessage

object InteractiveMessage:
    extension (str: String) {
        def toTextMessageUnsafe = TextMessage(NonEmptyString.unsafeFrom(str))
    }