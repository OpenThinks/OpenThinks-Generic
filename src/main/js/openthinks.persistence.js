/**
 * Structured Data Persistence library for Web SQL Database.
 *
 * openthinks.utils.js depended.
 *
 * @author Zhang Junlong
 *
 */
var ot = ot ? ot : {};
ot.persistence = ot.persistence ? ot.persistence : new OTPersistence();
ot.persistence.helper = ot.persistence.helper ? ot.persistence.helper : {};
ot.persistence.helper.queryParameterHelper = ot.persistence.helper.queryParameterHelper ? ot.persistence.helper.queryParameterHelper : new QueryParameterHelper();

function OTPersistence() {
    this._dbConfig = null;
    this._db = null;
    this.debug = false;
}
/**
 * Check if a multiple thread operation is done.
 *
 * @param threadCnt
 * @param callback
 * @return {Boolean}
 */
OTPersistence.prototype.isDone = function (threadCnt, callback) {
    if (0 == threadCnt) {
        if (callback)
            callback();

        return true;
    } else {
        return false;
    }
};
/**
 * Configure database information
 * @param dbConfig
 */
OTPersistence.prototype.config = function (dbConfig) {
    this._dbConfig = dbConfig;
};
/**
 * Open Web SQL database
 *
 * @returns database
 */
OTPersistence.prototype.openDatabase = function (callback) {
    var _this = this;
    try {
        if (!window.openDatabase) {
            console.error('Web SQL database not supported.');
            return null;
        } else {
            if (null == _this._db) {
                var dbParam = {
                    shortName: _this._dbConfig.name,
                    version: _this._dbConfig.version,
                    displayName: _this._dbConfig.displayName,
                    maxSize: _this._dbConfig.maxSize
                };

                _this._db = openDatabase(dbParam.shortName, dbParam.version, dbParam.displayName,
                    dbParam.maxSize);
            }

            if (callback) {
                callback(_this._db);
            }

            return _this._db;
        }
    } catch (e) {
        if (e == 'INVALID_STATE_ERR')
            console.error("Invalid database version.");
        else
            console.error("Unknown error " + e + ".");

        return null;
    }
};
OTPersistence.prototype.dropDb = function () {
    // Web SQL Database doesn't support drop database.
};
OTPersistence.prototype.dropTable = function (table) {
    var _this = this;
    var tables = null;
    if (table instanceof Array)
        tables = table;
    else
        tables = [table];

    for (var i = 0; i < tables.length; i++) {
        var dropSql = 'DROP TABLE IF EXISTS `' + tables[i] + '`';
        _this.execSql(dropSql, null);
    }
};
OTPersistence.prototype.execScript = function (scripts, callback) {
    var _this = this;
    var sqlArr = scripts.split(';');
    var cnt = 0;
    var complete = function () {
        cnt++;
        if (cnt == sqlArr.length)
            if (callback)
                callback();
    };
    for (var i = 0; i < sqlArr.length; i++) {
        if (null != sqlArr[i] && '' != sqlArr[i].trim())
            _this.execSql(sqlArr[i], null, function () {
                complete();
            });
        else
            complete();
    }
};
OTPersistence.prototype.execSql = function (sql, values, callback, errorCallback) {
    var _this = this;

    this.openDatabase(function (db) {
        db.transaction(function (txn) {
            txn.executeSql(sql, values, function (tx, sqlResultSet) {
                if (_this.debug)
                    console.log('Exec SQL(Async): ', sql, values);

                if (callback) {
                    callback(sqlResultSet);
                    if (_this.debug) {
                        console.log('Exec result: ', sqlResultSet);
                    }
                }
            }, function (tx, e) {
                if (_this.debug)
                    console.error(sql, values, e);

                if (errorCallback)
                    errorCallback(e);

            });
        });
    });
};
OTPersistence.prototype.query = function (sql, values, size, page, callback, errorCallback) {
    var _this = this;

    this.openDatabase(function (db) {
        db.readTransaction(function (txn) {
            // pagination count
            var cntSql = 'SELECT COUNT(1) TOTAL_CNT FROM ( ';
            cntSql += sql;
            cntSql += ' )';

            txn.executeSql(cntSql, values, function (cntTx, cntSqlResultSet) {
                if (_this.debug)
                    console.log('Async query:', cntSql, values);

                var totalCount = cntSqlResultSet.rows.item(0).TOTAL_CNT;

                if (null != size && 0 < size && null != page)
                    sql += ' limit ' + size + ' offset ' + size * (page - 1);

                txn.executeSql(sql, values, function (tx, sqlResultSet) {
                    if (_this.debug)
                        console.log('Async query:', sql, values);

                    if (callback) {
                        var pageRes = _this._paginateResults(sqlResultSet, size, page, totalCount);
                        callback(pageRes);
                        if (_this.debug)
                            console.log('Paginated result:', pageRes);
                    }
                }, function (tx, e) {
                    if (errorCallback)
                        errorCallback(e);

                    if (_this.debug)
                        console.error(sql, values, e);
                });

            }, function (cntTx, e) {
                if (errorCallback)
                    errorCallback(e);

                if (_this.debug)
                    console.error(cntSql, values, e);
            });
        });
    });
};
OTPersistence.prototype.read = function (tblName, id, callback) {
    this.find(tblName, 'id', id, function (resultSet) {
        var result;
        if (null != resultSet && resultSet.length > 0)
            result = resultSet[0];

        if (callback)
            callback(result)
    });
};
OTPersistence.prototype.readAll = function (tblName, callback) {
    var sql = 'SELECT * FROM ' + tblName;
    this.query(sql, null, null, 0, function (sr) {
        if (null != sr)
            callback(sr.resultSet);
        else
            callback();
    });
};
OTPersistence.prototype.readFirst = function (tblName, field, id, callback) {
    this.find(tblName, field, id, function (resultSet) {
        var result = null;
        if (null != resultSet && resultSet.length > 0)
            result = resultSet[0];

        if (callback)
            callback(result)
    });
};
OTPersistence.prototype.find = function (tblName, fields, values, callback) {
    var fldLst, valLst = null;
    var sql = 'SELECT * FROM ' + tblName + ' WHERE ';
    if (fields instanceof Array) {
        fldLst = fields;
        valLst = values;
    } else {
        fldLst = [fields];
        valLst = [values];
    }

    for (var i = 0; i < fldLst.length; i++)
        sql += fldLst[i] + '=? AND ';

    this.query(sql.substring(0, sql.length - 4), valLst, null, null, function (paginatedRes) {
        if (callback) {
            var rows = paginatedRes.resultSet;
            var resultSet = [];
            for (var i = 0; i < rows.length; i++)
                resultSet.push(ot.utils.object.hierarchize(rows[i]));

            callback(resultSet);
        }
    });
};
OTPersistence.prototype._paginateResults = function (sqlResultSet, size, page, totalCount) {
    if (this.debug)
        console.log(sqlResultSet, size, page);

    var paginatedResult = new OTPaginatedResult();

    if (null != sqlResultSet) {
        var rows = sqlResultSet.rows;
        if (null != rows) {
            var resArray = [];
            for (var i = 0; i < rows.length; i++)
                resArray.push(rows.item(i));

            if (null == size || size <= 0) {
                paginatedResult.resultSet = resArray;
                return paginatedResult;
            }

            paginatedResult.page = page;
            paginatedResult.totalPages = 1;
            paginatedResult.recordsCount = totalCount;

            if (paginatedResult.recordsCount % size == 0)
                paginatedResult.totalPages = paginatedResult.recordsCount / size;
            else
                paginatedResult.totalPages = Math.floor(paginatedResult.recordsCount / size) + 1;

            paginatedResult.resultSet = resArray;

            return paginatedResult;
        }
    }

    return null;
};
/**
 * Persist an object to specified table.
 */
OTPersistence.prototype.persist = function (obj, tblName, callback) {
    var _this = this;
    var _threadCnt = 0;

    var sql = 'INSERT INTO ' + tblName + '(';
    var values = [];
    for (var key in obj) {
        var columnName = key;
        if (typeof (obj[key]) == 'object') {
            columnName += '_id';
            if (null != obj[key]) {
                if (obj[key] instanceof Array) {
                    columnName = '';
                    var sTbl = tblName + '_' + key.substring(0, key.length - 1);
                    if (obj[key].length > 0 && sTbl) {
                        var m2mKey = key;
                        //var subTbl = key.substring(0, key.length - 1);
                        _threadCnt++;
                        _this.del(sTbl, [tblName + '_id'], [obj.id], function () {
                            // Many-to-many field
                            var subSql = 'INSERT INTO ' + sTbl + ' VALUES (?,?)';
                            for (var i = 0; i < obj[m2mKey].length; i++) {
                                var sValues = [];
                                sValues.push(obj.id);
                                sValues.push(obj[m2mKey][i].id);
                                _this.execSql(subSql, sValues, function () {
                                    _threadCnt--;
                                    _this.isDone(_threadCnt, callback);
                                });

                                // Insert nested list cascade
                                // _this.forcePersist(obj[m2mKey][i],m2mKey.substring(0,m2mKey.length-1));
                            }
                        });
                    }
                } else {
                    values.push(obj[key].id);
                }
            } else {
                values.push(obj[key]);
            }
        } else {
            values.push(obj[key]);
        }

        if (null != columnName && '' != columnName)
            sql = sql + columnName + ',';
    }
    sql = sql.substring(0, sql.length - 1);
    sql += ') VALUES(';
    for (var j = 0; j < values.length; j++)
        sql += '?,';

    sql = sql.substring(0, sql.length - 1);
    sql += ')';

    this.execSql(sql, values, function () {
        _this.isDone(_threadCnt, callback)
    });
};
OTPersistence.prototype.merge = function (obj, tblName, callback) {
    var _this = this;
    var sql = 'UPDATE ' + tblName + ' SET ';
    var values = [];
    for (var key in obj) {
        var columnName = key;
        if (typeof (obj[key]) == 'object') {
            columnName += '_id';
            if (null != obj[key]) {
                if (obj[key] instanceof Array) {
                    columnName = '';
                    var sTbl = tblName + '_' + key.substring(0, key.length - 1);
                    if (obj[key].length > 0 && sTbl) {
                        var m2mKey = key;
                        //var subTbl = key.substring(0, key.length - 1);
                        _this.del(sTbl, [tblName + '_id'], [obj.id], function () {
                            // Many-to-many field
                            var subSql = 'INSERT INTO ' + sTbl + ' VALUES (?,?)';
                            for (var i = 0; i < obj[m2mKey].length; i++) {
                                var sValues = [];
                                sValues.push(obj.id);
                                sValues.push(obj[m2mKey][i].id);
                                _this.execSql(subSql, sValues);

                                // Insert nested list cascade
                                _this.forcePersist(obj[m2mKey][i], m2mKey.substring(0, m2mKey.length - 1));
                            }
                        });
                    }
                } else {
                    values.push(obj[key].id);
                }
            } else {
                values.push(obj[key]);
            }
        } else {
            values.push(obj[key]);
        }

        if (null != columnName && '' != columnName)
            sql = sql + columnName + '=?,';
    }
    sql = sql.substring(0, sql.length - 1) + 'WHERE id=?';
    values.push(obj.id);
    this.execSql(sql, values, callback);
};
OTPersistence.prototype.forcePersist = function (obj, tblName, callback) {
    var _this = this;
    _this.remove(tblName, obj.id, function () {
        _this.persist(obj, tblName, callback)
    });
};
OTPersistence.prototype.remove = function (tblName, id, callback) {
    this.del(tblName, "id", id, callback);
};
OTPersistence.prototype.del = function (tblName, fields, values, callback) {
    var fldLst, valLst = null;
    var sql = 'DELETE FROM ' + tblName + ' WHERE ';
    if (fields instanceof Array) {
        fldLst = fields;
        valLst = values;
    } else {
        fldLst = [fields];
        valLst = [values];
    }

    for (var i = 0; i < fldLst.length; i++)
        sql += fldLst[i] + '=? AND ';

    this.execSql(sql.substring(0, sql.length - 4), valLst, callback);
};
OTPersistence.prototype.buildQueryParameters = function (params) {
    var queryParams = [];
    for (var i = 0; i < params.length; i++) {
        var fe = params[i];
        if (null != fe.value && '' != fe.value && fe.value != -1) {
            if (fe.value instanceof Array) {
                var cbParam = new OTComboQueryParameter();
                for (var x = 0; x < fe.value.length; x++) {
                    var tmpParam = new OTQueryParameter();
                    tmpParam.ro = 'OR';
                    tmpParam.field = fe.name;
                    tmpParam.value = fe.value[x];
                    cbParam.add(tmpParam);
                }
                queryParams.push(cbParam);
            } else if (fe.value.contains(',')) {
                var values = fe.value.split(',');
                var cbParam = new OTComboQueryParameter();
                for (var j = 0; j < values.length; j++) {
                    var tmpParam = new OTQueryParameter();
                    tmpParam.ro = 'OR';
                    tmpParam.field = fe.name;
                    tmpParam.value = values[j];
                    cbParam.add(tmpParam);
                }
                queryParams.push(cbParam);
            } else {
                var qParam = new OTQueryParameter();
                qParam.field = fe.name;
                qParam.value = fe.value;
                queryParams.push(qParam);
            }
        }
    }

    return queryParams;
};
function OTPaginatedResult() {
    this.resultSet = null;
    this.page = 1;
    this.size = 0;
    this.totalPages = 1;
    this.recordsCount = 0;
}
function OTQueryParameter() {
    this.ro = "AND"; //Relational Operator
    this.field = "1";
    this.co = "="; //Conditional Operator
    this.value = "1";
}
OTQueryParameter.prototype.toQlString = function (prefixStr) {
    var prefix = '';
    if (prefixStr)
        prefix = prefixStr;

    if (null != this.field && '' != this.field) {
        if (this.co.toUpperCase().match(/IS/i) && '' == this.value)
            this.value = null;

        if (ot.utils.object.isString(this.value))
            this.value = "'" + this.value + "'";

        return ' ' + prefix + this.ro + ' ' + this.field + ' ' + this.co + ' ' + this.value;
    } else {
        return '';
    }
};
function OTComboQueryParameter() {
    this.ro = 'AND';
    this._queryParameterSet = [];
}
OTComboQueryParameter.prototype.add = function (queryParameter) {
    this._queryParameterSet.push(queryParameter);
};
OTComboQueryParameter.prototype.toQlString = function (prefixStr) {
    var prefix = '';
    if (prefixStr)
        prefix = prefixStr;

    var str = '';
    for (var i = 0; i < this._queryParameterSet.length; i++)
        str += this._queryParameterSet[i].toQlString(prefix);

    if (null != str && '' != str.trim())
        return this.ro + ' (' + str.replace(/^ OR|^ AND/, '') + ')';
    else
        return '';
};

function QueryParameterHelper() {
    this.buildWhereClause = function (queryParameters, schema, integrally) {
        var clause = '';
        if (queryParameters)
            for (var i = 0; i < queryParameters.length(); i++) {
                //param.switchNamingConvention(schema);
                clause += queryParameters[i].toQlString();
            }

        if (integrally)
            return this.addWhereKeyword(clause);
        else
            return clause;
    };
    this.buildWhereClause = function (queryParameters, integrally) {
        var clause = '';

        if (queryParameters)
            for (var i = 0; i < queryParameters.length; i++)
                clause += queryParameters[i].toQlString();

        if (integrally)
            return this.addWhereKeyword(clause);
        else
            return clause;
    };
    this.addWhereKeyword = function (clause) {
        var paramNumberAnd, paramNumberOr = 0;
        var tmpAndStringArray = clause.toLowerCase()
            .split(" and ");
        paramNumberAnd = tmpAndStringArray.length - 1;
        for (var i = 0; i < tmpAndStringArray.length; i++) {
            var tmpOrStringArray = tmpAndStringArray[i].split(" or ");
            paramNumberOr += tmpOrStringArray.length - 1;
        }

        // Delete entire clause if there is only a "OR" clause
        if ((1 == paramNumberAnd + paramNumberOr) && (1 == paramNumberOr))
            clause = '';

        if (clause.length > 0)
            clause = clause.trim().replace(/^OR|^AND/i, " WHERE ");

        return clause;
    };
    this.toJSONFragment = function (queryParams, propName) {
        var prop = 'params';
        var tmp = '';
        if (propName)
            prop = propName;
        $.each(queryParams, function (i, p) {
            var field = p.field;
            var value = p.value;
            var ro = p.ro;
            var co = p.co;
            if (p instanceof OTComboQueryParameter) {
                field = p._queryParameterSet[0].field;
                value = '';
                ro = p._queryParameterSet[0].ro;
                co = p._queryParameterSet[0].co;
                for (var x = 0; x < p._queryParameterSet.length; x++)
                    value += ',' + p._queryParameterSet[x].value;

                value = value.substring(1, value.length);
            }
            var l = '"' + prop + '[' + i + '].field":"' + field + '",';
            l += '"' + prop + '[' + i + '].values":"' + value + '",';
            l += '"' + prop + '[' + i + '].ro":"' + ro + '",';
            l += '"' + prop + '[' + i + '].co":"' + co + '",';
            tmp += l;
        });

        return tmp;
    };
}