package com.consolebuilder.renderer

import cats.Applicative
import com.consolebuilder.domain.TextMessage

trait Renderer[F[_]] {
  def render(input: TextMessage): F[Unit]
}

object Renderer {
    def of[F[_]: Applicative](): Renderer[F] = new Renderer[F] {
      override def render(input: TextMessage): F[Unit] = Applicative[F].pure(println(input))

    }
}
