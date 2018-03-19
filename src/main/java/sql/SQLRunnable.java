/*
 * Copyright (C) 2018 BankAccount - All Rights Reserved
 *
 * Unauthorized copying of this file, via any median is strictly prohibited
 * proprietary and confidential. For more information, please contact me at
 * connor@hollasch.net
 *
 * Written by Connor Hollasch <connor@hollasch.net>, March 2018
 */

package sql;

import java.sql.SQLException;

/**
 * @author Connor Hollasch
 * @since Mar 19, 4:19 AM
 */
public interface SQLRunnable<T>
{
    T produce () throws SQLException;
}
