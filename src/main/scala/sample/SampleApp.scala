package sample

import java.util.Optional
import org.seasar.doma.jdbc.Result

object SampleApp extends App {
  lazy val dao: EmpDao = new EmpDaoImpl(AppConfig)
  lazy val tx = AppConfig.getTransactionManager
  lazy val NOT_ASSIGNED_EMP_ID = ID[Emp](-1)
  lazy val INITIAL_VERSION = -1

  tx.required({ () =>
    dao.create() // create table
    Seq(
      Emp(NOT_ASSIGNED_EMP_ID, "scott", 10, INITIAL_VERSION),
      Emp(NOT_ASSIGNED_EMP_ID, "allen", 20, INITIAL_VERSION)
    ).map {
      dao.insert
    }
    // idが2のEmpのageを +1
    val updated: Optional[Result[Emp]] = // JavaのOptionalを利用する場合型推論が効かないので明示する
      dao
        .selectById(ID(2)) // 検索して
        .map { emp =>
          dao.update(emp.growOld) // 見つかった場合には年齢を＋１して更新
        }
    println(updated) // => Optional[Result(entity=Emp(ID(2),allen,21,2), count=1)]
    val list = dao.selectAll
    list.forEach(println)
  // =>
  //   Emp(ID(1),scott,10,1)
  //   Emp(ID(2),allen,21,2)
  }: Runnable)

}
