package com.consolebuilder.domain

import eu.timepit.refined.types.string.NonEmptyString

opaque type TextMessage = String

object TextMessage:
    def apply(input: NonEmptyString): TextMessage = input.value