import mysql.connector
import json
import pyalex
from pyalex import Works, Authors, Sources, Institutions, Topics, Publishers, Funders, Concepts
pyalex.config.email = "1430186906@qq.com"
# 连接到 MySQL 数据库
conn = mysql.connector.connect(
    host="39.105.221.80",  # 数据库主机
    user="scholar",  # 数据库用户名
    password="scholar",  # 数据库密码
    database="scholar",  # 数据库名
    port=3306
)

# 创建一个游标对象
cursor = conn.cursor()

for i in range(10):

    json_data = Works().random()
    #     json_data = Works().random()
    authorships_list = json_data.get("authorships")
    concepts_list = json_data.get("concepts")
    referenced_works = json_data.get("referenced_works")

    len1 = len(authorships_list)
    len2 = len(concepts_list)
    len4 = len(referenced_works)

    print("Start!")

    #     插入works_referenced_works
    for h in range(len4):
        json_data7 = Works()[referenced_works[h]]
        data7 = (
            json_data.get("id"),
            referenced_works[h],
            json_data7.get("display_name")
        )
        #         print(data7)
        sql7 = """
        INSERT INTO openalex_works_referenced_works (work_id,referenced_work_id,referenced_work_name)
        VALUES (%s, %s, %s)
        """
        try:
            # 执行 SQL 插入语句
            cursor.execute(sql7, data7)
            # 提交事务
            conn.commit()
        #             print("插入works_referenced_works成功")
        except mysql.connector.Error as err:
            # 如果发生错误，回滚事务
            print(f"错误: {err}")
            conn.rollback()

    for h in range(len4):
        json_data7 = Works()[referenced_works[h]]
        data7 = (
            json_data.get("id"),
            referenced_works[h],
            json_data7.get("display_name")
        )
        sql7 = """
        INSERT INTO openalex_works_referenced_works (work_id,referenced_work_id,referenced_work_name)
        VALUES (%s, %s, %s)
        """
        try:
            # 执行 SQL 插入语句
            cursor.execute(sql7, data7)
            # 提交事务
            conn.commit()
        #             print("数据插入成功")
        except mysql.connector.Error as err:
            # 如果发生错误，回滚事务
            print(f"错误: {err}")
            conn.rollback()

    data = (
        json_data.get("id"),  # 确保是 str
        json_data.get("doi"),  # 确保是 str
        json_data.get("title"),  # 确保是 str
        json_data.get("display_name"),  # 确保是 str
        int(json_data.get("publication_year", 0)) if json_data.get("publication_year") is not None else None,
        # 确保是 int 或 None
        json_data.get("publication_date"),  # 确保是 str
        json_data.get("type"),  # 确保是 str
        int(json_data.get("cited_by_count", 0)) if json_data.get("cited_by_count") is not None else None,
        # 确保是 int 或 None
        1 if json_data.get("is_retracted") else 0,  # 确保是 tinyint(1)
        1 if json_data.get("is_paratext") else 0,  # 确保是 tinyint(1)
        json_data.get("cited_by_api_url"),  # 确保是 str 或 None
        json.dumps(json_data.get("abstract_inverted_index", {})),  # 转换为 JSON 字符串
        json_data.get("language"),  # 确保是 str
        json.dumps(json_data.get("grants", {})),
        json.dumps(json_data.get("keywords", {})),

        json.dumps(json_data.get("best_oa_location", {})),  # 转换为 JSON 字符串
        json.dumps(json_data.get("biblio", {})),  # 转换为 JSON 字符串
        json.dumps(json_data.get("primary_topic", {})),  # 转换为 JSON 字符串

    )

    # 插入数据的 SQL 语句
    sql = """
    INSERT INTO openalex_works (id, doi, title, display_name, publication_year, publication_date, type, cited_by_count, is_retracted, is_paratext, cited_by_api_url, abstract_inverted_index, language,grants,keywords,best_oa_location,biblio,primary_topic)
    VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s,%s,%s,%s,%s,%s)
    """
    try:
        # 执行 SQL 插入语句
        cursor.execute(sql, data)
        # 提交事务
        conn.commit()
    #         print("openalex_works数据插入成功")
    except mysql.connector.Error as err:
        # 如果发生错误，回滚事务
        print(f"错误: {err}")
        conn.rollback()

    # 'open_access': {'is_oa': True,
    # 'oa_status': 'bronze',
    # 'oa_url': 'https://doi.org/10.1016/s1933-2874(07)00319-4',
    # 'any_repository_has_fulltext': False},

    print("Start OpenAccess!")
    access_data = json_data.get("open_access"),
    json_data3 = access_data[0]
    data3 = (
        json_data.get("id"),
        1 if json_data3.get("is_oa") else 0,  # 确保是 tinyint(1)
        json_data3.get("oa_url") if json_data3.get("oa_url") is not None else None,  # 确保是 str 或 None
        json_data3.get("oa_status") if json_data3.get("oa_status") is not None else None  # 确保是 str
    )

    # 插入数据的 SQL 语句
    sql3 = """
    INSERT INTO openalex_openaccess (work_id, is_oa,oa_url,oa_status)
    VALUES (%s, %s, %s, %s)
    """
    try:
        # 执行 SQL 插入语句
        cursor.execute(sql3, data3)
        # 提交事务
        conn.commit()
    #         print("openalex_openaccess数据插入成功")
    except mysql.connector.Error as err:
        # 如果发生错误，回滚事务
        print(f"错误: {err}")
        conn.rollback()

    for j in range(len1):
        json_data1 = authorships_list[j]
        author_id = json_data1.get("author").get("id")
        json_data2 = Authors()[author_id]

        data2 = (
            json_data2.get("id"),
            json_data2.get("orcid") if json_data2.get("orcid") is not None else None,
            json_data2.get("display") if json_data2.get("display") is not None else None,
            json.dumps(json_data2.get("display_name_alternatives", {})),
            int(json_data2.get("works_count")) if json_data2.get("works_count") is not None else None,
            int(json_data2.get("cited_by_count")) if json_data2.get("cited_by_count") is not None else None,
            json_data2.get("last_known_institution") if json_data2.get("last_known_institution") is not None else None,
            json_data2.get("works_api_url") if json_data2.get("works_api_url") is not None else None,
            json_data2.get("updated_date") if json_data2.get("updated_date") is not None else None,
        )
        sql2 = """
        INSERT INTO openalex_authors (id, orcid,display_name,display_name_alternatives,works_count,cited_by_count,last_known_institution,works_api_url,updated_date)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s)
        """
        try:
            # 执行 SQL 插入语句
            cursor.execute(sql2, data2)
            # 提交事务
            conn.commit()
        #             print("openalex_authors数据插入成功")
        except mysql.connector.Error as err:
            # 如果发生错误，回滚事务
            print(f"错误: {err}")
            conn.rollback()
        # 将对应用户数据导入authors表中
        data1 = (
            json_data.get("id"),
            json_data1.get("author_position"),
            json_data1.get("author").get("id"),
            json.dumps(json_data1.get("institutions", {})),
            json_data1.get("raw_affiliation_string")
        )
        #         print(type(json_data1.get("institutions")))
        #         Works()["https://openalex.org/W3001118548"].get("authorships")[1].get("institutions")[1]
        sql1 = """
        INSERT INTO openalex_works_authorships (work_id, author_position,author_id,institutions,raw_affiliation_string)
        VALUES (%s, %s, %s, %s, %s)
        """
        try:
            # 执行 SQL 插入语句
            cursor.execute(sql1, data1)
            # 提交事务
            conn.commit()
        #             print("openalex_works_authorships数据插入成功")
        except mysql.connector.Error as err:
            # 如果发生错误，回滚事务
            print(f"错误: {err}")
            conn.rollback()

        if (json_data1.get("institutions") is not None):
            #         插入insitutions
            len10 = len(json_data1.get("institutions"))
            for x in range(len10):
                institution = Institutions()[json_data1.get("institutions")[x].get("id")]
                print(institution.get("id"))
                data10 = (
                    institution.get("id"),
                    institution.get("ror") if institution.get("ror") is not None else None,
                    institution.get("display_name") if institution.get("display_name") is not None else None,
                    institution.get("type") if institution.get("type") is not None else None,
                    institution.get("homepage_url") if institution.get("homepage_url") is not None else None,
                    institution.get("country_code") if institution.get("country_code") is not None else None,
                    institution.get("image_url") if institution.get("image_url") is not None else None,
                    institution.get("image_thumbnail_url") if institution.get(
                        "image_thumbnail_url") is not None else None,
                    json.dumps(institution.get("display_name_acronyms", {})),
                    json.dumps(institution.get("display_name_alternatives", {})),
                    int(institution.get("works_count")) if institution.get("works_count") is not None else None,
                    int(institution.get("cited_by_count")) if institution.get("cited_by_count") is not None else None,
                    institution.get("works_api_url") if institution.get("works_api_url") is not None else None,
                    institution.get("updated_date") if institution.get("updated_date") is not None else None,
                )

                sql10 = """
                INSERT INTO openalex_institutions (id,ror,display_name,country_code,type,homepage_url,image_url,image_thumbnail_url,display_name_acronyms,display_name_alternatives,works_count,cited_by_count,works_api_url,updated_date)
                VALUES (%s, %s, %s, %s, %s,%s,%s,%s,%s,%s,%s,%s,%s,%s)
                """
                try:
                    # 执行 SQL 插入语句
                    cursor.execute(sql10, data10)
                    # 提交事务
                    conn.commit()
                #             print("openalex_works_authorships数据插入成功")
                except mysql.connector.Error as err:
                    # 如果发生错误，回滚事务
                    print(f"错误: {err}")
                    conn.rollback()

    #     插入openalex_works_concepts、concepts、concepts_related_concepts
    for j in range(len2):
        json_data2 = concepts_list[j]
        concept_id = json_data2.get("id")
        json_data3 = Concepts()[concept_id]

        #         concepts
        data3 = (
            concept_id,
            json_data3.get("wikidata") if json_data3.get("wikidata") is not None else None,
            json_data3.get("display_name") if json_data3.get("display_name") is not None else None,
            int(json_data3.get("level")) if json_data3.get("level") is not None else None,
            json_data3.get("description") if json_data3.get("description") is not None else None,
            int(json_data3.get("works_count")) if json_data3.get("works_count") is not None else None,
            int(json_data3.get("cited_by_count")) if json_data3.get("cited_by_count") is not None else None,
            json_data3.get("image_url") if json_data3.get("image_url") is not None else None,
            json_data3.get("image_thumbnail_url") if json_data3.get("image_thumbnail_url") is not None else None,
            json_data3.get("works_api_url") if json_data3.get("works_api_url") is not None else None,
            json_data3.get("updated_date") if json_data3.get("updated_data") is not None else None,
        )
        sql3 = """
        INSERT INTO openalex_concepts (id, wikidata,display_name,level,description,works_count,cited_by_count,image_url,image_thumbnail_url,works_api_url,updated_date)
        VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s,%s,%s)
        """
        try:
            # 执行 SQL 插入语句
            cursor.execute(sql3, data3)
            # 提交事务
            conn.commit()
        #             print("openalex_concepts数据插入成功")
        except mysql.connector.Error as err:
            # 如果发生错误，回滚事务
            print(f"错误: {err}")
            conn.rollback()

        #      openalex_works_concepts插入数据
        data4 = (
            json_data.get("id"),
            concept_id,
            float(json_data2.get("score")) if json_data2.get("score") is not None else None,
            json_data2.get("display_name") if json_data2.get("display_name") is not None else None,
            json_data2.get("wikidata") if json_data2.get("wikidata") is not None else None,
        )

        sql4 = """
        INSERT INTO openalex_works_concepts (work_id,concept_id,score,display_name,wikidata)
        VALUES (%s, %s, %s, %s, %s)
        """
        try:
            # 执行 SQL 插入语句
            cursor.execute(sql4, data4)
            # 提交事务
            conn.commit()
        #             print("openalex_works_concepts数据插入成功")
        except mysql.connector.Error as err:
            # 如果发生错误，回滚事务
            print(f"错误: {err}")
            conn.rollback()

cursor.close()
conn.close()