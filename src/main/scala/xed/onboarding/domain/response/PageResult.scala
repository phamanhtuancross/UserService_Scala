package xed.onboarding.domain.response

case class PageResult[+T](total: Long, records: Seq[T]) { }
