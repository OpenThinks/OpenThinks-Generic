package org.openthinks.generic.dao;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;


/**
 * @author Zhang Junlong
 * 
 */

@Transactional
public class GenericDaoTest extends AbstractSpringDaoTest {
	@Autowired
	private GenericDao<TestDataEntity, Integer> genericDao;

	private TestDataEntity testInstance;

	@Before
	public void prepareData() {
		testInstance = new TestDataEntity();
		testInstance.setId(0);
		testInstance.setName("new");
	}

	@Test
	public void testCrud() throws Exception {
		int id = 0;

		TestDataEntity instance = new TestDataEntity();
		instance.setId(id);
		instance.setName("new");

		genericDao.create(instance);

		id = instance.getId();

		TestDataEntity persistedInstance = genericDao.read(id);
		assertEquals(instance.getId(), persistedInstance.getId());

		instance.setName("modifed");
		genericDao.update(instance);

		persistedInstance = genericDao.read(id);
		assertEquals(instance.getName(), persistedInstance.getName());

		genericDao.delete(instance);
		persistedInstance = genericDao.read(id);
		assertNull(persistedInstance);

	}

	@Test
	public void testReadBySpecifiedSection() throws Exception {
		genericDao.create(testInstance);

		testInstance = new TestDataEntity();
		testInstance.setName("new");
		genericDao.create(testInstance);

		testInstance = new TestDataEntity();
		testInstance.setName("new2");
		genericDao.create(testInstance);

		String ql = "select t from TestDataEntity t";

		PaginatedResult<TestDataEntity> sr = genericDao.read(ql,
				10, 1);
		assertEquals(3, sr.getRecordsCount());

		ql = "select new org.openthinks.generic.dao.SimplifiedTestDataEntity(t.id) from TestDataEntity t";
		sr = genericDao.read(ql, 10, 1);

		assertEquals(3, sr.getRecordsCount());

		ql = "select count(t.id) from TestDataEntity t group by t.name";
		sr = genericDao.read(ql, 10, 1);

		assertEquals(2, sr.getRecordsCount());

		ql = "select count(t.id) from TestDataEntity t group by t.type";
		sr = genericDao.read(ql, 10, 1);

		assertEquals(1, sr.getRecordsCount());

		ql = "select count(t.id) from TestDataEntity t where id=111111111 group by t.type";
		sr = genericDao.read(ql, 10, 1);

		assertEquals(0, sr.getRecordsCount());

		ql = "select t.id from TestDataEntity t where id=111111111";
		sr = genericDao.read(ql, 10, 1);

		assertEquals(0, sr.getRecordsCount());
	}

	public void testReadBySpecifiedSectionWithSql() {
		String ql = "SELECT COUNT(1) FROM ( select p.id as id,p.brand_text as brand,p.styleNo as styleNo,p.name as name,p.price as price,p.detailPrice as detailPrice,p.originalPrice as orderingPrice,p.promotional as promotional,p.code as code,p.picture as picture,p.season as season,c.name as category,p.remark as remark,sum(o.quantity) as orderCount from (select * from productOrder po where po.account_id='zhangwenlong') o right join product p on o.product_id=p.id inner join category c on p.category_id=c.id  group by p.id order by p.styleNo  )";

		try {
			genericDao.read(ql, 10, 1);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	@Test
	public void testQuery() {
		try {
			genericDao.query("select t from TestDataEntity t");
		} catch (Exception e) {
			e.printStackTrace();

			fail(e.getMessage());
		}
	}
}

