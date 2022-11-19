package com.consolebuilder.renderer

import cats.Applicative
import com.consolebuilder.domain.TextMessage

import com.consolebuilder.domain.*

trait Renderer[F[_]] {
  def render(input: InteractiveMessage): F[Unit]
}

object Renderer {
    def of[F[_]: Applicative](): Renderer[F] = new Renderer[F] {
      override def render(input: InteractiveMessage): F[Unit] = input match
        case i @ TextMessage(_) => writeText(i)
      
      
      private def writeText(message: TextMessage) = Applicative[F].pure(println(Console.BLUE+message.s.value))

    }
}
