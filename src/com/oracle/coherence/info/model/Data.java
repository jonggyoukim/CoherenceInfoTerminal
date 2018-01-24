/*
 * File: Data.java
 *
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * The contents of this file are subject to the terms and conditions of 
 * the Common Development and Distribution License 1.0 (the "License").
 *
 * You may not use this file except in compliance with the License.
 *
 * You can obtain a copy of the License by consulting the LICENSE.txt file
 * distributed with this file, or by consulting https://oss.oracle.com/licenses/CDDL
 *
 * See the License for the specific language governing permissions
 * and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file LICENSE.txt.
 *
 * MODIFICATIONS:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 */

package com.oracle.coherence.info.model;

/**
 * An interface allowing storage and retrieval of Coherence related data from an
 * object.
 *
 * @author Tim Middleton
 */
public interface Data {
	/**
	 * Get a given column value for the column index.
	 *
	 * @param nColumn
	 *            the column index
	 *
	 * @return a given column value for the column index
	 */
	public Object getColumn(int nColumn);

	/**
	 * Sets a given column value for the column index
	 *
	 * @param nColumn
	 *            the column index
	 * @param oValue
	 *            the value to set
	 */
	public void setColumn(int nColumn, Object oValue);
}
