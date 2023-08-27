////////////////////////////////////////////////////////////////////////////////////////////////////
package com.jyis.bookmanager.ndl;
////////////////////////////////////////////////////////////////////////////////////////////////////
import java.io.Serializable;
import java.util.Date;
////////////////////////////////////////////////////////////////////////////////////////////////////
public class JobHistory implements Serializable
{
	/** JOB_INSTANCE_ID */
    private Long jobInstanceId;

	/** CREATE_TIME */
    private Date createTime;

	/** START_TIME */
    private Date startTime;

	/** END_TIME */
    private Date endTime;

	/** STATUS */
    private String status;

	/** EXIT_MESSAGE */
    private String exitMessage;
    //---------------------------------------------------------------------------------------------
    /**
     * jobInstanceId を取得する
     * @return jobInstanceId
     */
    public Long getJobInstanceId()
    {
        return jobInstanceId;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * jobInstanceId を設定する
     * @param arg jobInstanceId
     */
    public void setJobInstanceId(final Long arg)
    {
        jobInstanceId = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * createTime を取得する
     * @return createTime
     */
    public Date getCreateTime()
    {
        return createTime;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * createTime を設定する
     * @param arg createTime
     */
    public void setCreateTime(final Date arg)
    {
        createTime = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * startTime を取得する
     * @return startTime
     */
    public Date getStartTime()
    {
        return startTime;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * startTime を設定する
     * @param arg startTime
     */
    public void setStartTime(final Date arg)
    {
        startTime = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * endTime を取得する
     * @return endTime
     */
    public Date getEndTime()
    {
        return endTime;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * endTime を設定する
     * @param arg endTime
     */
    public void setEndTime(final Date arg)
    {
        endTime = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * status を取得する
     * @return status
     */
    public String getStatus()
    {
        return status;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * status を設定する
     * @param arg status
     */
    public void setStatus(final String arg)
    {
        status = arg;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * exitMessage を取得する
     * @return exitMessage
     */
    public String getExitMessage()
    {
        return exitMessage;
    }
    //---------------------------------------------------------------------------------------------
    /**
     * exitMessage を設定する
     * @param arg exitMessage
     */
    public void setExitMessage(final String arg)
    {
        exitMessage = arg;
    }
}