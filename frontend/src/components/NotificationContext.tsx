import { createContext, useContext } from 'react';
import { NotificationEnum } from '../types/notificationEnum';

interface NotificationContextType {
  addNotification: (
    notificationText: string,
    notificationType: NotificationEnum
  ) => void;
}

export const NotificationContext = createContext<
  NotificationContextType | undefined
>(undefined);

export const useNotification = () => {
  const ctx = useContext(NotificationContext);
  if (!ctx) {
    throw new Error(
      'useNotification must be used within a NotificationProvider'
    );
  }
  return ctx;
};
