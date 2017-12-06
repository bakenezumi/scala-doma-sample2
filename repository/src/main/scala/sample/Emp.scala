package sample

import scala.annotation.meta.field
import org.seasar.doma._

@Entity(immutable = true)
case class Emp(
  // Doma2ではフィールドにアノテーションを付ける必要があるが
  // コンストラクタパラメータに対してアノテーションを付けると
  // パラメータに対してのみ有効になる
  // 下記のように@fieldと明示することで自動的に作られる同名の
  // 状態フィールドに対し アノテーションを有効にできる。
  // http://www.scala-lang.org/api/current/scala/annotation/meta/index.html
  @(Id @field)
  @(GeneratedValue @field)(strategy = GenerationType.SEQUENCE)
  @(SequenceGenerator @field)(sequence = "emp_id_seq")
  id: ID[Emp],
  name: String,
  age: Int,
  @(Version @field)
  version: Int
) {
  def growOld: Emp = this.copy(age = this.age + 1)
}
