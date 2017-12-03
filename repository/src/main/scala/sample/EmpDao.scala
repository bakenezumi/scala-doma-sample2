package sample

import org.seasar.doma._

import java.util.Optional

import org.seasar.doma.jdbc.Result

// Scalaで作ったConfigはstaticメソッドを持てず
// アノテーション引数に指定できないため
// 利用者がコンストラクタパラメータで渡す
@Dao
trait EmpDao {
  @Script
  def create(): Unit

  @Select
  def selectById(id: ID[Emp]): Optional[Emp]

  @Select
  def selectAll: java.util.List[Emp]

  @Insert
  def insert(emp: Emp): Result[Emp]

  @Update
  def update(emp: Emp): Result[Emp]
}
